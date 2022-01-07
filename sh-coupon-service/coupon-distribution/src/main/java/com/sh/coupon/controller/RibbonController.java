package com.sh.coupon.controller;

import com.sh.coupon.annotation.IgnoreResposeAdvice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * ribbon 调用微服务的方式
 */
@Slf4j
@RestController
public class RibbonController {

    private final RestTemplate restTemplate;

    public RibbonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 通过模板组件调用template 微服务
     */
    @GetMapping("/info")
    @IgnoreResposeAdvice
    public TemplateInfo getTemplateInfo(){
        String infoUrl = "http://eureka-client-coupon-template/coupon-template/info";
        return restTemplate.getForEntity(infoUrl,TemplateInfo.class).getBody();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateInfo{
       private Integer code;
       private String msg;
        private List<Map<String,Object>> Data;
    }
}
