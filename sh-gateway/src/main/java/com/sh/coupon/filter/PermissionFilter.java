package com.sh.coupon.filter;


import com.alibaba.fastjson.JSON;
import com.sh.coupon.permission.PermissionClient;
import com.sh.coupon.vo.CheckPermissionRequest;
import com.sh.coupon.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 权限过滤器
 * @author admin
 */
@Slf4j
@Component
public class PermissionFilter extends AbsSecurityFilter {
    private final PermissionClient permissionClient;
    @Autowired
    public PermissionFilter(PermissionClient permissionClient) {
        this.permissionClient = permissionClient;
    }

    @Override
    protected boolean interceptCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //执行权限校验逻辑
        Long userId = Long.valueOf(request.getHeader("userId"));
        String uri = request.getRequestURI().substring("/coupon".length());
        String httpMethod = request.getMethod();
        return permissionClient.checkPermission(new CheckPermissionRequest(
                userId,uri,httpMethod
        )).booleanValue();
    }

    @Override
    protected int getState() {
        return HttpStatus.OK.value();
    }

    @Override
    protected String getErrorMsg() {
        CommonResponse response = new CommonResponse();
        response.setCode(-1);
        response.setMsg("您没有操作权限");
        return JSON.toJSONString(response);
    }
}
