package com.sh.coupon.executor.impl;

import com.sh.coupon.constant.RuleFlag;
import com.sh.coupon.executor.AbstractExecutor;
import com.sh.coupon.executor.RuleExecutor;
import com.sh.coupon.vo.CouponTemplateSDK;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 满减优惠卷结算规则执行器
 */
@Slf4j
@Component
public class ManJianExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN;
    }



    @Override
    public SettlementInfo couputeRule(SettlementInfo settlementInfo) {
        double goodsSum = retain2Decimals(goodsCostSum(settlementInfo.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlementInfo,goodsSum);
        // 如果不等于空，则意味着不匹配，可直接返回
        if (null != probability) {
            log.debug("ManJian Template Is Not Match To GoodsType!");
            return probability;
        }
        //判断是否符合满减规则
        CouponTemplateSDK templateSDK = settlementInfo.getCouponAndTemplateInfos().get(0).getCouponTemplate();
        double base = (double)templateSDK.getRule().getDiscount().getBase();
        double quota = (double)templateSDK.getRule().getDiscount().getQuota();
        //如果商品总价小于满减额度，则无法使用。直接返回
        if(goodsSum < base){
            settlementInfo.setCost(goodsSum);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            return settlementInfo;
        }
        settlementInfo.setCost( retain2Decimals((goodsSum - quota)) > minCost() ? retain2Decimals((goodsSum - quota)) : minCost());
        log.debug("Use ManJian Coupon Make Goods Cost From {} To {}",goodsSum,settlementInfo.getCost());
        return settlementInfo;
    }
}
