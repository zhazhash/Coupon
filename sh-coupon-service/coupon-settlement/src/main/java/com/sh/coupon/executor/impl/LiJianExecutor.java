package com.sh.coupon.executor.impl;

import com.sh.coupon.constant.RuleFlag;
import com.sh.coupon.executor.AbstractExecutor;
import com.sh.coupon.executor.RuleExecutor;
import com.sh.coupon.vo.CouponTemplateSDK;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 立减优惠卷规则处理器
 */
@Slf4j
@Component
public class LiJianExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.LIJIAN;
    }

    @Override
    public SettlementInfo couputeRule(SettlementInfo settlementInfo) {
        double goodsSum = retain2Decimals(goodsCostSum(settlementInfo.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlementInfo,goodsSum);
        if (null != probability) {
            log.debug("LiJian Template Is Not Match GoodsType!");
            return probability;
        }
        //立减不需要判断总金额
        CouponTemplateSDK couponTemplateSDK = settlementInfo.getCouponAndTemplateInfos().get(0).getCouponTemplate();
        double quota = couponTemplateSDK.getRule().getDiscount().getQuota();

        settlementInfo.setCost(retain2Decimals(goodsSum - quota) > minCost() ?
                retain2Decimals(goodsSum - quota) : minCost());
        log.debug("Use LiJian is Coupon Make Goods Cost From {} To {}",goodsSum,settlementInfo.getCost());
        return settlementInfo;
    }
}
