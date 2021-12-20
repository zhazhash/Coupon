package com.sh.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品类型枚举
 */
@Getter
@AllArgsConstructor
public enum GoodsType {
    SHENGXIAN("生鲜",1),
    WENYU("文娱",2),
    JIAJU("家居",3),
    OTHERS("其他",4),
    ALL("全品类",5);

    private String discription;
    private Integer code;
    public static GoodsType of (Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values()).filter(bean -> bean.code.equals(code)).findAny().orElseThrow(() -> new IllegalArgumentException(code + "not exists!"));
    }
}
