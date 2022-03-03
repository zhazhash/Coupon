package com.sh.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@Component
public class RateLimiterFilter extends AbstractPreZuulFilter{
    /*
    每秒可以获得两个令牌
     */
    RateLimiter rateLimiter = RateLimiter.create(2.0);
    @Override
    protected Object cRun() {
        HttpServletRequest request =requestContext.getRequest();
        if(rateLimiter.tryAcquire()){
            return success();
        }
        log.error("rate limit:{}",request.getRequestURI());
        return fail(402,"error rate limit");
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}
