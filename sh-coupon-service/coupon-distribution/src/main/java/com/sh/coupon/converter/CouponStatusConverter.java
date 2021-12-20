package com.sh.coupon.converter;

import com.sh.coupon.constant.CouponStatus;
import com.sh.coupon.entity.Coupon;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 用户优惠卷状态枚举转换类
 */
@Converter
public class CouponStatusConverter implements AttributeConverter<CouponStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer code) {
        return CouponStatus.of(code);
    }
}
