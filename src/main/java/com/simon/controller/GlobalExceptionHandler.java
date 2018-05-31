package com.simon.controller;

import com.simon.domain.ResultMsg;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 *
 * @author simon
 * @create 2018-04-25 23:25
 **/

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ExceptionHandlerExceptionResolver {
    private static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResultMsg grantError(HttpServletRequest request, Exception e){
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setCode(500);
        resultMsg.setMsg(e.getMessage());
        resultMsg.setData(e.toString());
        logger.error(e);
        return resultMsg;
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultMsg accessDeniedError(HttpServletRequest request, AccessDeniedException e){
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setCode(HttpStatus.FORBIDDEN.value());
        resultMsg.setMsg(e.getMessage());
        resultMsg.setData(e.toString());
        logger.error(e);
        return resultMsg;
    }
}
