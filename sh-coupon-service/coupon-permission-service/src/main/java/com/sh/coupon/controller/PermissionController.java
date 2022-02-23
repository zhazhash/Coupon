package com.sh.coupon.controller;

import com.sh.coupon.annotation.IgnoreResposeAdvice;
import com.sh.coupon.service.PathService;
import com.sh.coupon.service.PermissionService;
import com.sh.coupon.vo.CheckPermissionRequest;
import com.sh.coupon.vo.CreatePathRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 路径创建与权限校验对外服务接口实现
 * @author admin
 */
@Slf4j
@RestController
public class PermissionController {
    private final PathService pathService;
    private final PermissionService permissionService;

    public PermissionController(PathService pathService, PermissionService permissionService) {
        this.pathService = pathService;
        this.permissionService = permissionService;
    }


    @PostMapping("/create/path")
    public List<Integer> createPath(@Valid @RequestBody CreatePathRequest request){
        log.info("createPath:{}",request.getPathInfos().size());
        return pathService.createPath(request);
    }

    /**
     * 权限校验接口
     * @param request
     * @return
     */
    @IgnoreResposeAdvice
    @PostMapping("/check/permission")
    public Boolean checkPermission(@RequestBody CheckPermissionRequest request){
        log.info("checkPermission for args:{},{},{}",
                request.getUserId(),request.getUri(),request.getHttpMehod());
        return permissionService.checkPermission(
                request.getUserId(),request.getUri(),request.getHttpMehod()
        );
    }
}
