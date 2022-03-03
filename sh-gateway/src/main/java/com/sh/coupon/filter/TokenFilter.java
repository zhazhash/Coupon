package com.sh.coupon.filter;



import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class TokenFilter extends AbstractPreZuulFilter{
    @Override
    protected Object cRun() {
        HttpServletRequest request = requestContext.getRequest();
        log.info("method:{},url:{}",request.getMethod(),request.getRequestURI());
        String token = request.getParameter("token");
        if(StringUtils.isBlank(token)){
            log.error("token为空 ");
            return fail(401,"token为空");
        }
        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
