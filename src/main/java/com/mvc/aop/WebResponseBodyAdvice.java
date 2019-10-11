package com.mvc.aop;

import com.alibaba.fastjson.JSON;
import com.mvc.error.WebException;
import com.mvc.pojo.ErrorLog;
import com.mvc.service.ErrorHandle;
import com.mvc.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 返回数据统一封装
 */
@Component
@RestControllerAdvice
public class WebResponseBodyAdvice implements ResponseBodyAdvice {

    // 忽略controller层返回数据的封装
    public static String ignore = "ignore-web-pack";

    @Autowired(required = false)
    private ErrorHandle errorHandle;

    /**
     * 统一返回异常处理
     * @param e
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = {Exception.class})
    Result handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(this.errorHandle != null){
            this.errorHandle.handler(new ErrorLog(e, request));
        }

        return new Result(10001, e.getMessage(), null);

//        if(response.getHeader(ignore) != null){
//            throw e;
//        }else if(e instanceof WebException){
//            return new Result(((WebException) e).getCode(), e.getMessage(), null);
//        }else {
//            e.printStackTrace();
//            return new Result(10001, e.getMessage(), null);
//        }
    }


    /**
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();

        if(response.getHeader(ignore) != null){
            return false;
        }

        if(methodParameter.getMethod().getAnnotation(ExceptionHandler.class) != null){
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Result result = new Result(0, null, o);
        if(o instanceof String){
            return JSON.toJSONString(result);
        }
        return result;
    }

}
