package com.sh.coupon.service;

import com.sh.coupon.entity.CouponTemplate;

/**
 * 异步线程服务类
 */
public interface ISyncService {

    /**
     * 根据模板异步的创建优惠卷码
     * @param template {@link CouponTemplate} 优惠卷模板实体类
     */
    void syncConstructCouponByTemplate(CouponTemplate template);
}
