package com.sh.coupon.service;


import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.Coupon;

import java.util.List;

/**
 *优惠卷模板生成的cache操作
 */
public interface IRedisService {

    /**
     *根据userId和状态找到缓存的优惠卷列表数据
     * @param userId 用户id
     * @param status 优惠卷状态 {@link com.sh.coupon.constant.CouponStatus }
     * @return {@link Coupon}s ,可能会返回null，代表从来没有过记录
     */
    List<Coupon> getCacheCoupons(Long userId,Integer status);

    /**
     * 保存空的优惠卷列表到缓存中，用户避免缓存穿透
     * @param userId 用户id
     * @param status 优惠卷状态列表
     */
    void saveEmptyCouponListToCache(Long userId,List<Integer> status);

    /**
     * 尝试从cache中获取一个优惠卷码
     * @param templateId 优惠卷码主键
     * @return 优惠卷码
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * 将优惠卷保存到cache中
     * @param userId 用户id
     * @param coupons {@link Coupon}s
     * @param status 优惠卷状态
     * @return 保存成功的个数
     * @throws CouponException
     */
    Integer addCouponToCache(Long userId,List<Coupon> coupons,Integer status) throws CouponException;
}
