package com.sh.coupon.executor;

import com.sh.coupon.constant.RuleFlag;
import com.sh.coupon.vo.SettlementInfo;

/**
 * 优惠卷模板规则处理器接口定义
 */
public interface RuleExecutor {

    /**
     * 规则类型标记
     * @return {@link RuleFlag}
     */
    RuleFlag ruleConfig();

    /**
     * 优惠卷规则的计算
     * @param settlementInfo {@link SettlementInfo} 包含了选择的优惠卷
     * @return {@link SettlementInfo} 修正过的结算信息
     */
    SettlementInfo couputeRule (SettlementInfo settlementInfo);
}
