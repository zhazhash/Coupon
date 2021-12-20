package com.sh.coupon.service;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.CouponTemplate;
import com.sh.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠卷模板基础服务
 */
public interface ITemplateBaseService {

    /**
     * 根据优惠卷模板id 获取优惠卷模板信息
     * @param id 模板id
     * @return {@link CouponTemplate} 优惠卷模板实体
     * @throws CouponException
     */
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;

    /**
     * 查找所有可用的优惠卷模板
     * @return {@link CouponTemplateSDK}s
     */
    List<CouponTemplateSDK> findAllUsableTemplate();

    /**
     * 根据模板ids 获取优惠卷模板信息
     * @param ids 模板ids
     * @return Map<key:模板id,value: CouponTemplateSDK>
     */
    Map<Integer,CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids);
}
