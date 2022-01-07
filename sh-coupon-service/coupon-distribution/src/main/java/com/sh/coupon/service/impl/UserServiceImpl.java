package com.sh.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sh.coupon.constant.Constant;
import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.dao.CouponDao;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.Coupon;
import com.sh.coupon.feign.SettlementClient;
import com.sh.coupon.feign.TemplateClient;
import com.sh.coupon.service.IRedisService;
import com.sh.coupon.service.IUserService;
import com.sh.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    /*用户优惠卷实体操作类*/
    private final CouponDao couponDao;
    /* redis服务*/
    private final IRedisService redisService;
    /*kafka服务*/
    private final KafkaTemplate kafkaTemplate;
    /*优惠卷模板微服务*/
    private final TemplateClient templateClient;
    /*优惠卷结算微服务调用*/
    private final SettlementClient settlementClient;

    public UserServiceImpl(CouponDao couponDao, IRedisService redisService, TemplateClient templateClient, SettlementClient settlementClient, KafkaTemplate kafkaTemplate) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public List<Coupon> findCouponByStatus(Long userId, Integer status) throws CouponException {

        List<Coupon> cacheCoupons = redisService.getCacheCoupons(userId,status);
        if(CollectionUtils.isEmpty(cacheCoupons)){
            log.debug("coupon cache is empty :{},{}",userId,status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            if(CollectionUtils.isEmpty(dbCoupons)){
                log.debug("current user do not have coupon  :{},{}",userId,status);
                return Collections.emptyList();
            }
            Map<Integer, CouponTemplateSDK> couponTemplateSDK = templateClient.findIds2TemplateSDK(dbCoupons.stream().map(Coupon::getTemplateId).collect(Collectors.toList())).getData();
            dbCoupons.forEach(dc -> {
                dc.setCouponTemplateSDK(couponTemplateSDK.get(dc.getTemplateId()));
            });
            redisService.addCouponToCache(userId,dbCoupons,status);
            cacheCoupons = dbCoupons;

        }
        /*去掉redis填充默认的无效的优惠卷*/
        cacheCoupons = cacheCoupons.stream().filter(c -> c.getId() != -1).collect(Collectors.toList());

        /*如果当前获取的是可用优惠卷，则将剔除过期数据*/
        if (CouponStatus.of(status) == CouponStatus.USABLE){
            CouponClassify classify = CouponClassify.classify(cacheCoupons);
            if(CollectionUtils.isNotEmpty(classify.getExpired())){
                log.info("Add Expired Coupons To Cache From FindCouponsByStatus :{},{},{}",userId,classify.getExpired(),status);
                redisService.addCouponToCache(userId,classify.getExpired(),CouponStatus.EXPIRED.getCode());
                /*发送给kafka*/
                kafkaTemplate.send(Constant.TOPIC,new CouponKafkaMessage(CouponStatus.EXPIRED.getCode(),classify.getExpired().stream()
                .map(Coupon::getId).collect(Collectors.toList())));
            }
            return classify.getUsable();
        }

        return cacheCoupons;
    }

    @Override
    public List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException {

        long curTime = new Date().getTime();
        List<CouponTemplateSDK> templateSDKS = templateClient.findAllUsableTemplate().getData();
        log.debug("Find All Template (From templateclient) Count:{}",templateSDKS);
        templateSDKS = templateSDKS.stream().filter( t ->
            t.getRule().getExpiration().getDeadline()>=curTime
        ).collect(Collectors.toList());
        log.info("Find Usable Template Count :{}",templateSDKS.size());
        //key 为templateId pair key为 用户可领取次数，value为优惠卷模板
        Map<Integer, Pair<Integer,CouponTemplateSDK>> limit2Template = Maps.newHashMapWithExpectedSize(templateSDKS.size());

        templateSDKS.forEach(t -> {
            limit2Template.put(t.getId(),Pair.of(t.getRule().getLimitation(),t));
        });

        List<Coupon> userUsableCoupons = findCouponByStatus(userId,CouponStatus.USABLE.getCode());
        log.debug("Current User Has Usable Coupons:{},{}",userId,userUsableCoupons.size());

        Map<Integer,List<Coupon>> template2Coupons = userUsableCoupons.stream().collect(Collectors.groupingBy(Coupon::getTemplateId));
        List<CouponTemplateSDK> result = Lists.newArrayListWithCapacity(limit2Template.size());
        //遍历所有可领取的优惠卷， 判断哪些是当前用户还可以领取的
        limit2Template.forEach((k,v) ->{
            int limitation = v.getLeft();
            //判断当前用户是否已有此优惠卷，如果有判断当前用户领取此优惠卷是否上限
            if(template2Coupons.containsKey(k) && template2Coupons.get(k).size() >= limitation){
                return;
            }
            result.add(v.getRight());
        });

        return result;
    }

    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {
        Map<Integer, CouponTemplateSDK> id2Template = templateClient.findIds2TemplateSDK(
                Collections.singletonList(request.getCouponTemplateSDK().getId())).getData();
        if (id2Template.size() <= 0 ) {
            log.error("Can Not Acquire Template From TemplateClient :{}",request.getCouponTemplateSDK().getId());
            throw new CouponException("Can Not Acquire Template From TemplateClient");
        }
        CouponTemplateSDK idTemplate = id2Template.get(0);//将获取到的优惠卷模板取出
        //获取当前用户的优惠卷信息， 用于判断是否领取，与领取数量是否达到最大值
        List<Coupon> userUsableCoupons = findCouponByStatus(request.getUserId(),CouponStatus.USABLE.getCode());
        Map<Integer,List<Coupon >> templateId2Coupons = userUsableCoupons.stream().collect(Collectors.groupingBy(Coupon::getTemplateId));

        if (templateId2Coupons.containsKey(idTemplate.getId()) && templateId2Coupons.get(idTemplate.getId()).size() >= idTemplate.getRule().getLimitation()){
            log.error("Exceed Template Assign Limitation:{}",idTemplate.getId());
            throw  new CouponException("Exceed Template Assign Limitation");
        }
        //尝试从缓存中获取，可能为空，代表已经领光，无法在领取
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(idTemplate.getId());
        if (StringUtils.isBlank(couponCode)) {
            log.error("Can Not Acquire Copon Code :{}",idTemplate.getId());
        }
        //生成一个新的优惠卷信息，持久化到db
        Coupon newCoupon = new Coupon(idTemplate.getId(),request.getUserId(),couponCode,CouponStatus.USABLE);
        newCoupon = couponDao.save(newCoupon);
        newCoupon.setCouponTemplateSDK(idTemplate);
        //加入redis
        redisService.addCouponToCache(request.getUserId(),Collections.singletonList(newCoupon),CouponStatus.USABLE.getCode());
        return newCoupon;
    }

    @Override
    public SettlementInfo settlenment(SettlementInfo info) throws CouponException {
    //获取本次结算或核销时的优惠卷信息。
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = info.getCouponAndTemplateInfos();
        if(CollectionUtils.isEmpty(ctInfos)){
            log.info("Empty Coupons For Settle");
            double goodsSum = 0.0;
            for (GoodsInfo goodsInfo : info.getGoodsInfos()) {
                goodsSum += goodsInfo.getPrice() * goodsInfo.getCount();
            }
            //没有优惠卷就不需要核销
            info.setCost(retain2Decimals(goodsSum));
        }
        List<Coupon> coupons = findCouponByStatus(info.getUserId(),CouponStatus.USABLE.getCode());
        //  Function.identity()是对象本身
        Map<Integer,Coupon> id2Coupon = coupons.stream().collect(Collectors.toMap(Coupon::getId, Function.identity()));
        //如果当前用户的优惠卷为空或传递的优惠卷不是当前用户的子集，则返回错误
        if (MapUtils.isEmpty(id2Coupon) ||
                !CollectionUtils.isSubCollection(ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getId).collect(Collectors.toList()), id2Coupon.keySet())) {
            log.info("{}",id2Coupon.keySet());
            log.error("User Coupons Has Some Problem ,It Is Not SubCollection Of Coupons!");

            throw  new CouponException("User Coupons Has Some Problem ,It Is Not SubCollection Of Coupons!");
        }

        log.debug("Current Settlement Coupons Is User's :{}",ctInfos.size());
        List<Coupon> settleCoupons = new ArrayList<Coupon>(ctInfos.size());
        ctInfos.forEach(ci -> settleCoupons.add(id2Coupon.get(ci.getId())));
        //通过结算服务获取结算信息
        SettlementInfo processedInfo = settlementClient.computeRule(info).getData();
        //如果是核销，并且结算正确时处理(如果不可用于结算，结算微服务会将优惠卷信息清空)
        if (processedInfo.getEmploy() && CollectionUtils.isNotEmpty(processedInfo.getCouponAndTemplateInfos())){
            log.info("Settle User Coupons:{},{}",info.getUserId(), JSON.toJSONString(settleCoupons));
            redisService.addCouponToCache(info.getUserId(),settleCoupons,CouponStatus.USED.getCode());
            kafkaTemplate.send(Constant.TOPIC,new CouponKafkaMessage(CouponStatus.USED.getCode(),settleCoupons.stream().map(Coupon::getId).collect(Collectors.toList())));
        }
        return processedInfo;


    }

    /**
     * 保留两位小数
     * @param vaule
     * @return
     */
    private double retain2Decimals(double vaule){
        //BigDecimal.ROUND_HALF_UP 代表四舍五入
        return  new BigDecimal(vaule).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
