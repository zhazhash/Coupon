package com.sh.coupon.feign.hystrix;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.feign.TemplateClient;
import com.sh.coupon.vo.CommonResponse;
import com.sh.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 优惠卷模板熔断降级策略
 */
@Slf4j
@Component
public class TemplateCientHystrix implements TemplateClient {
    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate() throws CouponException {
        log.error("[eureka-client-coupon-template] findAllUsableTemplate request error.");
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-template] findAllUsableTemplate request error.",
                Collections.emptyList());
    }

    @Override
    public CommonResponse<Map<Integer, CouponTemplateSDK>> findIds2TemplateSDK(Collection<Integer> ids) throws CouponException {
        log.error("[eureka-client-coupon-template] findIds2TemplateSDK request error.");
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-template] findIds2TemplateSDK request error.",
                Collections.emptyMap());
    }
}
