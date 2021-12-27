package com.sh.coupon.feign;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.feign.hystrix.SettlementClientHystrix;
import com.sh.coupon.vo.CommonResponse;
import com.sh.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 优惠卷结算微服务
 */
@FeignClient(value = "eureka-client-coupon-settlement",fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    @RequestMapping(value = "/settlement/computerule",method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule (@RequestBody SettlementInfo settlementInfo) throws CouponException;
}
