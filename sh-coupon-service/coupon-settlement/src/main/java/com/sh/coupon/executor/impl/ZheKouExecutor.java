package com.sh.coupon.executor.impl;

import com.sh.coupon.constant.RuleFlag;
import com.sh.coupon.executor.AbstractExecutor;
import com.sh.coupon.executor.RuleExecutor;
import com.sh.coupon.vo.CouponTemplateSDK;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 折扣优惠卷规则执行器
 */
@Slf4j
@Component
public class ZheKouExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.ZHEKOU;
    }

    @Override
    public SettlementInfo couputeRule(SettlementInfo settlementInfo) {
        double goodsSum =  retain2Decimals(goodsCostSum(settlementInfo.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlementInfo,goodsSum);
        if (null != probability) {
            log.debug("ZheKou Template Is Not Match GoodsType!");
        }
        CouponTemplateSDK templateSDK = settlementInfo.getCouponAndTemplateInfos().get(0).getCouponTemplate();
        double quota = (double)templateSDK.getRule().getDiscount().getQuota();
        settlementInfo.setCost(retain2Decimals((goodsSum * (quota *1.0 / 100))) > minCost()
                ? retain2Decimals((goodsSum * (quota *1.0 / 100))) : minCost());
        log.debug("Use ZheKou Coupon Make Goods Cost From {} To {}",goodsSum,settlementInfo.getCost());
        return settlementInfo;
    }
}
