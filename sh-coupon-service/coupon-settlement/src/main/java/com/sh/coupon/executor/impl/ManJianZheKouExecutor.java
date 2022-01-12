package com.sh.coupon.executor.impl;

import com.alibaba.fastjson.JSON;
import com.sh.coupon.constant.CouponCategory;
import com.sh.coupon.constant.RuleFlag;
import com.sh.coupon.executor.AbstractExecutor;
import com.sh.coupon.executor.RuleExecutor;
import com.sh.coupon.vo.GoodsInfo;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 满减和折扣同时使用的优惠卷规则执行器
 */
@Slf4j
@Component
public class ManJianZheKouExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * 重写此方法，判断多种优惠卷是否存在
     * @param settlementInfo
     * @return
     */
    @Override
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlementInfo) {
        List<Integer> goodsType = settlementInfo.getGoodsInfos().stream().map(GoodsInfo::getType).collect(Collectors.toList());
        List<Integer> templateGoodsType = new ArrayList<>();

        settlementInfo.getCouponAndTemplateInfos().forEach(ct -> {
            templateGoodsType.addAll(JSON.parseObject(ct.getCouponTemplate().getRule()
                    .getUsage().getGoodsType(),List.class));
        });
        // 如果要使用多种优惠卷，就要包含所有的商品类型，差值为空 CollectionUtils.subtract用于判断两个集合的差值有哪些
        return CollectionUtils.isEmpty(CollectionUtils.subtract(goodsType,templateGoodsType));
    }

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN_ZHEKOU;
    }

    @Override
    public SettlementInfo couputeRule(SettlementInfo settlementInfo) {
        double goodsSum = retain2Decimals(goodsCostSum(settlementInfo.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlementInfo,goodsSum);
        if(null != probability){
            log.debug("ManJian And ZheKou Template Is Not Match GoodsType!");
            return probability;
        }
        SettlementInfo.CouponAndTemplateInfo manJian = null;
        SettlementInfo.CouponAndTemplateInfo zheKou = null;
        //此处无需关心是否获取到，校验与实现分离
        for (SettlementInfo.CouponAndTemplateInfo ct : settlementInfo.getCouponAndTemplateInfos()) {
            if(CouponCategory.of(ct.getCouponTemplate().getCategory()) == CouponCategory.MANJIAN){
                manJian = ct;
            }else {
                zheKou = ct;
            }
        }
        assert null != manJian;
        assert null != zheKou;

        //当前的满减卷与折扣卷是否可以一起使用，不能共用则清空优惠卷，返回商品总价
        if (!isTemplateCanShared(manJian,zheKou)){
            settlementInfo.setCost(goodsSum);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            return settlementInfo;
        }
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        double targetSum = goodsSum;
        //判断是否可以使用满减优惠卷
        double manJianBase = (double)manJian.getCouponTemplate().getRule().getDiscount().getBase();
        double manJianQuota = (double) manJian.getCouponTemplate().getRule().getDiscount().getQuota();

        if(goodsSum >= manJianBase){
            targetSum -= manJianQuota;
            ctInfos.add(manJian);
        }

        double zheKouQuota = (double) zheKou.getCouponTemplate().getRule().getDiscount().getQuota();
        ctInfos.add(zheKou);
        targetSum *= zheKouQuota * 1.0 / 100;

        settlementInfo.setCost(retain2Decimals(targetSum > minCost() ? targetSum : minCost()));
        settlementInfo.setCouponAndTemplateInfos(ctInfos);
        log.debug("Use ManJian Add ZheKou Coupon Make Goods Cost From {} To {}" ,goodsSum,settlementInfo.getCost());
        return settlementInfo;
    }

    /**
     * 判断两张优惠卷是否可以共用
     * @param manJian
     * @param zheKou
     * @return
     */
    private boolean isTemplateCanShared(SettlementInfo.CouponAndTemplateInfo manJian,SettlementInfo.CouponAndTemplateInfo zheKou){
        String manJianKey = manJian.getCouponTemplate().getKey() + String.format("%04d" ,manJian.getCouponTemplate().getId());
        String zheKouKey = zheKou.getCouponTemplate().getKey() + String.format("%04d",zheKou.getCouponTemplate().getId());
        List<String> allSharedKeysForManJian = new ArrayList<>();
        allSharedKeysForManJian.addAll(JSON.parseObject(manJian.getCouponTemplate().getRule().getWeight(),List.class));
        allSharedKeysForManJian.add(manJianKey);
        List<String> allSharedKeysForZheKou = new ArrayList<>();
        allSharedKeysForZheKou.add(zheKouKey);
        allSharedKeysForZheKou.addAll(JSON.parseObject(zheKou.getCouponTemplate().getRule().getWeight(),List.class));
        return CollectionUtils.isSubCollection(Arrays.asList(manJianKey,zheKouKey),allSharedKeysForManJian) ||
                CollectionUtils.isSubCollection(Arrays.asList(manJianKey,zheKouKey),allSharedKeysForZheKou);
    }


}

