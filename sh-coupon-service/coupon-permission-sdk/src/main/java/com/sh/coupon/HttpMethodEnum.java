package com.sh.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * http方法类型枚举
 */
@Getter
@AllArgsConstructor
public enum HttpMethodEnum {
    GET,
    POST,
    HEAD,
    PUT,
    PATHC,
    DELETE,
    OPTIONS,
    TRACE,
    ALl
}
