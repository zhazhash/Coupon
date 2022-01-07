package com.sh.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则类型枚举定义
 */
@Getter
@AllArgsConstructor
public enum RuleFlag {
    //单类别优惠卷定义
    MANJIAN("满减卷的计算规则"),
    ZHEKOU("折扣卷的计算规则"),
    LIJIAN("立减卷的计算规则"),

    //多类别的优惠卷定义

    MANJIAN_ZHEKOU("满减 + 折扣的计算规则");

    /*规则描述*/
    private String description;
}
