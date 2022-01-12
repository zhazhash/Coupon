package com.sh.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.executor.ExecutorManger;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.CodecException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SettlementController {
    /*结算规则执行管理器*/
    private final ExecutorManger executorManger;

    public SettlementController(ExecutorManger executorManger) {
        this.executorManger = executorManger;
    }

    /**
     * 优惠卷结算
     * @param settlementInfo
     * @return
     * @throws CodecException
     */
    @PostMapping("/settlement/computerule")
    public SettlementInfo computeRule(@RequestBody SettlementInfo settlementInfo) throws CouponException {
        log.info("settlement:{}", JSON.toJSONString(settlementInfo));
        return executorManger.couputeRule(settlementInfo);
    }
}
