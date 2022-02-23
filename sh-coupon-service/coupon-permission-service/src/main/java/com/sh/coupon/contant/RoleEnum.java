package com.sh.coupon.contant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 * @author admin
 */

@Getter
@AllArgsConstructor
public enum RoleEnum {

    SUPER_ADMIN("超级管理员"),
    ADMIN("管理员"),
    CUSTOMER("普通用户");

    private String roleName;
}
