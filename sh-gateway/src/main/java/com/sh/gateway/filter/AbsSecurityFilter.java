package com.sh.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 抽象权限过滤器
 * @author admin
 */
@Slf4j
public abstract class AbsSecurityFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();
        //如果前一个 filter 执行失败，不会调用之后的filter
        return response.getStatus() == 0 ||
                response.getStatus() == HttpStatus.SC_OK;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        log.info("过滤器开始执行，过滤器为：{} ,过滤url为：{}",this.getClass().getSimpleName(),request.getRequestURI());
        boolean result = false;
        try {
            result = interceptCheck(request,response);
        } catch (Exception e) {
            log.error("过滤器异常，过滤器为: {} , 过滤请求为：{} , 报错信息为：{}"
            ,this.getClass().getSimpleName(),
                    request.getRequestURI(),e.getMessage());
        }
        log.info("过滤器执行结束，过滤器为：{}, 过滤结果为:{}",this.getClass().getSimpleName(),result);
        if(!result){
            //对当前的请求不进行路由,此请求不会转发到后台
            context.setSendZuulResponse(false);
            response.setHeader("Content-type","application/json;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            response.setStatus(getState());
            try {
                response.getWriter().write(getErrorMsg());
                context.setResponse(response);
            } catch (IOException e) {
                log.error("过滤器异常，过滤器为: {} , 过滤请求为：{} , 报错信息为：{}"
                        ,this.getClass().getSimpleName(),
                        request.getRequestURI(),e.getMessage());
            }
        }
        return null;
    }

    /**
     *  由子类实现权限校验
     * @param request
     * @param response
     * @return true:通过 ，false：未通过
     * @throws Exception
     */
    protected abstract boolean interceptCheck(HttpServletRequest request,
                                              HttpServletResponse response) throws Exception;

    /**
     * 由子类response的返回码
     * @return 状态码
     */
    protected abstract int getState();

    /**
     * 由子类实现 报错时提示的错误信息
     * @return json格式错误数据
     */
    protected abstract String getErrorMsg();

}
