package com.sh.coupon.converter;

import com.sh.coupon.constant.ProductLine;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 产品线类型 枚举类型转换器
 */
@Converter
public class ProductLineConverter  implements AttributeConverter<ProductLine,Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductLine productLine) {
        return productLine.getCode();
    }

    @Override
    public ProductLine convertToEntityAttribute(Integer integer) {
        return ProductLine.of(integer);
    }
}
