package com.sh.coupon.dao;

import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户优惠卷表接口定义
 */
public interface CouponDao extends JpaRepository<Coupon, Integer> {
    /**
     * 基于userId和status 查询，
     * 相当于使用sql  where userId = '' and status = ''
     * @param userId
     * @param status
     * @return
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
