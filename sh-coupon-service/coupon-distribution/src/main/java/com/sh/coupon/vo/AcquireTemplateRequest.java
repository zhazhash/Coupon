package com.sh.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取优惠卷请求对象
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {
    /**用户id*/
    private Long userId;
    /**优惠卷模板信息*/
    private CouponTemplateSDK templateSDK;
}
