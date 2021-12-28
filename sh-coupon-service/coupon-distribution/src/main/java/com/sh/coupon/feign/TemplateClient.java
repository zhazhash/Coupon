package com.sh.coupon.feign;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.feign.hystrix.TemplateCientHystrix;
import com.sh.coupon.vo.CommonResponse;
import com.sh.coupon.vo.CouponTemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠卷模板微服务调用
 */
@FeignClient(value = "eureka-client-coupon-template",fallback = TemplateCientHystrix.class)
public interface TemplateClient {

    @RequestMapping(value = "/template/sdk/all",method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate() throws CouponException;

    @RequestMapping(value = "/template/sdk/infos",method = RequestMethod.GET)
    CommonResponse<Map<Integer,CouponTemplateSDK>> findIds2TemplateSDK(@RequestParam("ids") Collection<Integer> ids) throws CouponException;
}
