package com.sh.coupon.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sh.coupon.ecxeption.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 健康检查接口
 */
@Slf4j
@RestController
public class HealthCheck {
    /*服务发现客户端 ：获取所有在当前的eureka server中注册的客户端*/
    private final DiscoveryClient client;
    /*服务注册接口，提供了获取服务 id的方法*/
    private final Registration registration;

    @Autowired
    public HealthCheck(DiscoveryClient client, Registration registration) {
        this.client = client;
        this.registration = registration;
    }

    /**
     * 健康检查接口
     * 127.0.0.1:7001/coupon-template/health
     */
    @GetMapping("/health")
    public String health(){
        log.info("view health api");
        return "CouponTemplate Is Ok !";
    }

    /**
     * 异常接口测试
     * 127.0.0.1:7001/coupon-template/exception
     * @return
     * @throws CouponException
     */
    @GetMapping("/exception")
    public String exception () throws CouponException{
        log.info("view exception api");
        throw new CouponException("CouponTemplate Has Some Problem");
    }

    /**
     * 获取 Eureka Server 上微服务的元信息
     * 127.0.0.1:7001/coupon-template/info
     * @return
     */
    @GetMapping("/info")
    public List<Map<String,Object>> info(){
       List<ServiceInstance> instances =  client.getInstances(registration.getServiceId());
       List<Map<String,Object>> result = Lists.newArrayList();
        instances.forEach(t -> {
            Map<String,Object> map = Maps.newHashMap();
            map.put("serviceid",t.getServiceId());
            map.put("host",t.getHost());
            map.put("instanceId",t.getInstanceId());
            map.put("port",t.getPort());
            map.put("cheme",t.getScheme());
            result.add(map);
        });
        return result;
    }
}
