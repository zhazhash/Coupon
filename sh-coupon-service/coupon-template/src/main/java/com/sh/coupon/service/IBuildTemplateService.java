package com.sh.coupon.service;


import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.CouponTemplate;
import com.sh.coupon.vo.TemplateRequest;

/**
 * 构建优惠卷模板接口定义
 */
public interface IBuildTemplateService {

    /**
     *
     * @param request {@link TemplateRequest} 模板信息请求实体
     * @return {@link CouponTemplate} 优惠卷模板实体
     * @throws CouponException
     */
    CouponTemplate buildTemplate (TemplateRequest request) throws CouponException;
}
