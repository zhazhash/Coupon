package com.sh.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 产品线类型
 */
@Getter
@AllArgsConstructor
public enum ProductLine {

    TIANMAO("天猫",1),
    TIAOBAO("淘宝",2);
    /**产品线描述*/
    private String description;
    private Integer code ;

    public static ProductLine of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not esists!"));
    }
}
