package com.sh.coupon.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限描述注解： 定义Controller 接口的权限
 * @author admin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShCouponPermission {
    /**
     * 接口描述信息
     */
    String description() default "";

    /**
     * 此接口是否为制度，默认是true
     */
    boolean readOnly() default true;

    /**
     * 扩展属性
     *  最好以JSON格式去存储
     */
    String extra() default "";
}
