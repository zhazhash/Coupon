package com.sh.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sh.coupon.constant.Constant;
import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.Coupon;
import com.sh.coupon.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {
    /*Redis 客户端*/
    private final StringRedisTemplate redisTemplate;
    @Autowired
    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Coupon> getCacheCoupons(Long userId, Integer status) {
        log.info("Get CouponS From Cache:{},{}",userId,status);
        String redisKey = status2RedisKey(status,userId);
        List<String> couponStr = redisTemplate.opsForHash().values(redisKey)
                .stream().map( o -> Objects.toString(o,null))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponStr)){
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        return couponStr.stream().map(s -> JSON.parseObject(s,Coupon.class)).collect(Collectors.toList());
    }

    @Override
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty List To Cache For User:{}, Status{}",userId,JSON.toJSONString(status));
        Map<String,String> map = Maps.newHashMap();
        map.put("-1", JSON.toJSONString(Coupon.invalidCoupon()));

        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                status.forEach(s -> {
                    String redisKey = status2RedisKey(s,userId);
                    redisOperations.opsForHash().putAll(redisKey,map);
                });
                return null;
            }
        };
        //pipelined 支持将命令统一发送至redis并统一返回结果
    log.info("Pipelined Exe Result : {}", JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));

    }



    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {
        String redisKey = String.format("%s%s",Constant.RedisPrefix.COUPON_TEMPLATE,templateId);
        String couponCode = redisTemplate.opsForList().leftPop(redisKey);

        log.info("Acquire Coupon Code :{},{},{}",templateId,redisKey,couponCode);
        return couponCode;
    }

    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException {
        log.info("Add Coupon To Cache : {},{},{}",userId,JSON.toJSONString(coupons),status);
        CouponStatus couponStatus = CouponStatus.of(status);
        Integer result = -1;
        switch (couponStatus){
            case EXPIRED:
                result = addCouponToCacheForExpired(userId,coupons);
                break;
            case USABLE:
                result = addCouponToCacheForUsable(userId,coupons);
                break;
            case USED:
                result = addCouponToCacheForUsed(userId,coupons);
                break;
        }
        return result;
    }

    /**
     * 添加已过期的优惠卷信息到redis，并修改可用的缓存
     * @param userId
     * @param coupons
     * @return
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForExpired(Long userId, List<Coupon> coupons) {
        log.debug("Add Coupon To Cache For Expired.");
        Map<String,String> needCacheForExpired = new HashMap<String, String>(coupons.size());
        String redisKeyForUsable = status2RedisKey(CouponStatus.USABLE.getCode(),userId);
        String redisKeyForExpired = status2RedisKey(CouponStatus.EXPIRED.getCode(),userId);
        List<Coupon> curUasbleCoupons = getCacheCoupons(userId,CouponStatus.USABLE.getCode());
        //修改的优惠卷必须比未使用的优惠卷少
        assert curUasbleCoupons.size() > coupons.size();

        coupons.forEach(coupon -> needCacheForExpired.put(
                coupon.getId().toString(),JSON.toJSONString(coupon)
        ));

        List<Integer> curUasbleIds = curUasbleCoupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramsIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        if(!CollectionUtils.isSubCollection(paramsIds,curUasbleIds)){
            log.error("CurCoupons Is Not Equal ToCache :{},{},{}",userId,JSON.toJSONString(curUasbleIds),JSON.toJSONString(paramsIds));
        }
        List<String > needCleanKey = paramsIds.stream().map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisTemplate.opsForHash().putAll(redisKeyForExpired,needCacheForExpired);
                redisTemplate.opsForHash().delete(redisKeyForUsable,needCleanKey.toArray());
                redisTemplate.expire(redisKeyForExpired,getRandomExpirationTime(1,2),TimeUnit.SECONDS);
                redisTemplate.expire(redisKeyForUsable,getRandomExpirationTime(1,2),TimeUnit.SECONDS);

                return null;
            }
        };
        log.info("Pipeline Exe Result:{}",redisTemplate.executePipelined(sessionCallback));
        return coupons.size();
    }

    /**
     * 添加已使用的优惠卷信息到redis ，并修改可用的缓存
     * @param userId
     * @param coupons
     * @return
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForUsed(Long userId, List<Coupon> coupons) {
        log.debug("Add Coupon To Cache For Used .");
        String redisKeyForUsable = status2RedisKey(CouponStatus.USABLE.getCode(),userId);
        String redisKeyForUsed = status2RedisKey(CouponStatus.USED.getCode(),userId);
        Map<String ,String> needCacheForUsed = new HashMap<String, String>(coupons.size());
        //获取当前可用的优惠卷个数
       List<Coupon> curUsableCoupons = getCacheCoupons(userId,CouponStatus.USABLE.getCode());

       assert curUsableCoupons.size() > coupons.size();

       coupons.forEach(c -> needCacheForUsed.put(c.getId().toString(),JSON.toJSONString(c)));

       //校验当前优惠卷是否与缓存中的匹配
        List<Integer> curUasbleIds = curUsableCoupons.stream().map(Coupon::getId).collect(Collectors.toList()); //获取map中的所有key作为list
        List<Integer> paramsIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
       //判断a是否是b的子集 ，如果是则返回true
       if(!CollectionUtils.isSubCollection(coupons,curUsableCoupons)){
            log.error("CurCoupons Is Not Equal ToCache :{},{},{}",userId,JSON.toJSONString(curUasbleIds),JSON.toJSONString(paramsIds));
       }
        List<String > needCleanKey = paramsIds.stream().map(i -> i.toString()).collect(Collectors.toList());
       SessionCallback sessionCallback = new SessionCallback() {
           @Override
           public Object execute(RedisOperations redisOperations) throws DataAccessException {
               redisTemplate.opsForHash().putAll(redisKeyForUsed,needCacheForUsed); //插入到已使用缓存中。
               redisTemplate.opsForHash().delete(redisKeyForUsable,needCleanKey); //将未使用的数据删除
               //重置过期时间
               redisTemplate.expire(redisKeyForUsed,getRandomExpirationTime(1,2),TimeUnit.SECONDS);
               redisTemplate.expire(redisKeyForUsable,getRandomExpirationTime(1,2),TimeUnit.SECONDS);
               return null;
           }
       };

       log.info("Pipeline Exe Result:{}",redisTemplate.executePipelined(sessionCallback));
       return coupons.size();
    }

    /**
     * 添加可使用的优惠卷信息到redis
     * @param userId
     * @param coupons
     * @return
     */
    private Integer addCouponToCacheForUsable(Long userId, List<Coupon> coupons) {
        log.debug("Add Coupon To Cache For Usable");
        Map<String,String> needCacheObject = Maps.newHashMap();
        coupons.forEach(c -> needCacheObject.put(c.getId().toString(),JSON.toJSONString(c)));
        String redisKey = status2RedisKey(CouponStatus.USABLE.getCode(),userId);
        redisTemplate.opsForHash().putAll(redisKey,needCacheObject);
        log.info("Add {} ,Coupons To Cache :{},{}",needCacheObject.size(),userId,redisKey);
        //设置超时时间
        redisTemplate.expire(redisKey,getRandomExpirationTime(1,2), TimeUnit.SECONDS);
        return needCacheObject.size();
    }

    /**
     * 获取随机的过期时间
     * 防止redis缓存雪崩
     * @param min 最小过期时间
     * @param max 最大过期时间
     * @return 返回min，max之间的随机秒数
     */
    private Long getRandomExpirationTime(Integer min , Integer max){

        return RandomUtils.nextLong(min * 60 * 60 ,max * 60 * 60);
    }

    /**
     * 根据status获取对应的key
     * * @param code
     * @param userId
     * @return
     */
    private String status2RedisKey(Integer code, Long userId) {
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(code);
        switch (couponStatus){
            case USED:
                redisKey = String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USED,userId);
                break;
            case USABLE:
                redisKey = String.format("%s%s",Constant.RedisPrefix.USER_COUPON_USABLE,userId);
                break;
            case EXPIRED:
                redisKey = String.format("%s%d",Constant.RedisPrefix.USER_COUPON_EXPIRED,userId);
                break;
        }
        return redisKey;
    }
}
