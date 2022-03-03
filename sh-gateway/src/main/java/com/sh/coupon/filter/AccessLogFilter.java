package com.sh.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccessLogFilter extends AbstractPreZuulFilter{
    @Override
    protected Object cRun() {
        Long startTime = (Long) requestContext.get("startTime");
        String uri = requestContext.getRequest().getRequestURI();
        long duration = System.currentTimeMillis() - startTime;
        log.info("uri: {}, duration :{}",uri,duration);
        return success();
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}
