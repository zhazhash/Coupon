package com.sh.coupon.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public abstract class AbstractRouteZuulFilter extends AbstractZuulFilter{
    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }
}
