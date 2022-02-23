package com.sh.coupon;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 为了引入 permission-sdk 而添加的配置类
 * @author admin
 */
@Configuration
@EnableFeignClients
public class PermissionSdkConfig {
}
