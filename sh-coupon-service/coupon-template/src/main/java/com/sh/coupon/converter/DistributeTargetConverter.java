package com.sh.coupon.converter;

import com.sh.coupon.constant.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *  分发目标枚举类型转换器
 */
@Converter
public class DistributeTargetConverter  implements AttributeConverter<DistributeTarget,Integer> {
    @Override
    public Integer convertToDatabaseColumn(DistributeTarget distributeTarget) {
        return distributeTarget.getCode();
    }

    @Override
    public DistributeTarget convertToEntityAttribute(Integer integer) {
        return DistributeTarget.of(integer);
    }
}
