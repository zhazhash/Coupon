package com.sh.coupon.advice;

import com.sh.coupon.annotation.IgnoreResposeAdvice;
import com.sh.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应
 * RestControllerAdvice 用于controller的增强
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice {

    /**
     * 判断是否需要对响应进行处理
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //如果当前方法使用了忽略统一响应注解，则不处理
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResposeAdvice.class)){
            return false;
        }
        //如果当前方法的类上使用了忽略统一响应注解，则不处理
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResposeAdvice.class)){
            return false;
        }
        return true;
    }

    /**
     * 响应返回之前的处理
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        CommonResponse<Object> response = new CommonResponse<Object>(0,"");
        if (null == o){
            return o;
        }
        if (o instanceof CommonResponse){
            response = (CommonResponse<Object>) o;
            return response;
        }
        response.setData(o);
        return response;
    }
}
