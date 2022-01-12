package com.sh.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.entity.CouponTemplate;
import com.sh.coupon.service.IBuildTemplateService;
import com.sh.coupon.service.ITemplateBaseService;
import com.sh.coupon.vo.CouponTemplateSDK;
import com.sh.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/template")
public class CouponTemplateController {
    private final IBuildTemplateService buildTemplateService;
    private final ITemplateBaseService templateBaseService;
    @Autowired
    public CouponTemplateController(IBuildTemplateService buildTemplateService, ITemplateBaseService templateBaseService) {
        this.buildTemplateService = buildTemplateService;
        this.templateBaseService = templateBaseService;
    }

    /**
     *  构建优惠卷模板
     *  127.0.0.1/coupon/coupon-template/template/build
     * @param request
     * @return
     */
    @PostMapping("/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request) throws CouponException {
        log.info("Build Template : {}", JSON.toJSONString(request));
        return buildTemplateService.buildTemplate(request);
    }
    /**
     *  构建优惠卷模板详情
     *  127.0.0.1:9000/coupon/coupon-template/template/info
     * @param
     * @return
     */
    @GetMapping("/info")
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException{
        log.info("build Template Info For :{}", id);
        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     *获取所有可用的优惠卷模板
     * 127.0.0.1:9000/coupon/coupon-template/template/sdk/all
     * @return
     * @throws CouponException
     */
    @GetMapping("/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate() throws  CouponException{
        log.info("Find All Usable Template");
        return templateBaseService.findAllUsableTemplate();
    }

    /**
     * 根据模板ids 获取优惠卷模板信息
     * @param ids
     * 127.0.0.1:9000/coupon/coupon-template/template/sdk/infos
     * @return
     * @throws CouponException
     */
    @GetMapping("/sdk/infos")
    public Map<Integer,CouponTemplateSDK> findIds2TemplateSDK(@RequestParam("ids") Collection<Integer> ids) throws CouponException{
        log.info("findIds2TemplateSDK : {}",JSON.toJSONString(ids));
        return templateBaseService.findIds2TemplateSDK(ids);
    }

}
