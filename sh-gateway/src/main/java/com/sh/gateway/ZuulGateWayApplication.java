package com.sh.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关应用启动
 * @EnableZuulProxy  标识当前应用是zuul Server
 * @SpringCloudApplication 组合了springboot应用注解 + 服务发现注解 + 熔断注解
 */
@EnableZuulProxy
@SpringCloudApplication
public class ZuulGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGateWayApplication.class,args);
    }
}
