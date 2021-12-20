package com.sh.coupon.constant;

/**
 * 常量定义
 */
public class Constant {
    /*kafka消息的topic*/
     public final static String TOPIC = "sh_user_coupon_op";

    /**
     * Redis key 前缀
     */
     public static class RedisPrefix{
         /*优惠卷模板key前缀*/
         public final static String COUPON_TEMPLATE = "sh_coupon_template_code_";
        /*用户当前可用的 key的前缀*/
         public final static String USER_COUPON_USABLE = "sh_user_coupon_usable_";
        /*用户当前所有已使用的优惠卷key的前缀*/
         public final static String USER_COUPON_USED = "sh_user_coupon_used_";
        /*用户当前所有已过期的优惠卷的key的前缀*/
         public final static String USER_COUPON_EXPIRED = "sh_user_coupon_expired_";

     }
}
