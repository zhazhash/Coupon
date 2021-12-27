package com.sh.coupon.feign.hystrix;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.feign.SettlementClient;
import com.sh.coupon.vo.CommonResponse;
import com.sh.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 优惠卷结算微服务熔断降级策略
 */
@Slf4j
@Component
public class SettlementClientHystrix  implements SettlementClient {

    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlementInfo) throws CouponException {
        log.error("[eureka-client-coupon-settlement] computeRule request error.");
        settlementInfo.setEmploy(false);
        settlementInfo.setCost(-1.0);
        return new CommonResponse<>(-1,"[eureka-client-coupon-settlement] computeRule request error.",settlementInfo);
    }
}
