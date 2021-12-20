package com.sh.coupon.service;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.Coupon;
import com.sh.coupon.vo.AcquireTemplateRequest;
import com.sh.coupon.vo.CouponTemplateSDK;

import java.util.List;

/**
 * 用户服务相关的接口定义
 * 1.用户三类状态优惠卷信息展示
 * 2.查看用户当前可领取的优惠卷模板
 * 3.用户领取优惠卷
 * 4.用户消费优惠卷服务
 */
public interface IUserService {

    /**
     * 根据用户id和状态获取优惠卷记录
     * @param userId 用户id
     * @param status 状态
     * @return {@link Coupon}s
     */
    List<Coupon> findCouponByStatus(Long userId,Integer status) throws CouponException;

    /**
     * 根据用户id获取当前可领取的优惠卷模板
     * @param userId
     * @return {@link CouponTemplateSDK}s
     * @throws CouponException
     */
    List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException;

    /**
     * 用户领取优惠卷
     * @param request {@link AcquireTemplateRequest}
     * @return {@link Coupon}
     * @throws CouponException
     */
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;
}
