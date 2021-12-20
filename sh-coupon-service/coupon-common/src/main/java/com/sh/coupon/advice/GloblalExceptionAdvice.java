package com.sh.coupon.advice;

import com.sh.coupon.ecxeption.CouponException;
import com.sh.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GloblalExceptionAdvice {

    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handlerCouponException(HttpServletRequest req, CouponException ce){
        CommonResponse<String> response = new CommonResponse<>(-1,"business error");
        response.setData(ce.getMessage());
        return  response;
    }


}
