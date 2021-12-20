package com.sh.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取优惠卷请求对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {
    /*用户id*/
    private Integer userId;
    /*优惠卷模板信息*/
    private CouponTemplateSDK couponTemplateSDK;
}
