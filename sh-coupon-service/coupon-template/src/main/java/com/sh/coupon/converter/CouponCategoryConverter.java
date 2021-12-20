package com.sh.coupon.converter;

import com.sh.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 优惠卷分类枚举属性转换器
 * AttributeConverter<CouponCategory,String> 第一个为枚举类，第二个为数据库中类型
 */
@Converter
public class CouponCategoryConverter implements AttributeConverter<CouponCategory,String> {
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    @Override
    public CouponCategory convertToEntityAttribute(String s) {
        return CouponCategory.of(s);
    }
}
