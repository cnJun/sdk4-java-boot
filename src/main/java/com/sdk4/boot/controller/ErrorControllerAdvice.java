package com.sdk4.boot.controller;

import com.alibaba.fastjson.JSON;
import com.sdk4.boot.AjaxResponse;
import com.sdk4.boot.CommonErrorCode;
import com.sdk4.boot.exception.DaoException;
import com.sdk4.common.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 错误拦截
 *
 * Created by sh on 2018/8/16.
 */
@Slf4j
@RestControllerAdvice
public class ErrorControllerAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public String errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return error(request, response, e);
    }

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public String runtimeErrorHandler(HttpServletRequest request, HttpServletResponse response, RuntimeException e) {
        return error(request, response, e);
    }


    private String error(HttpServletRequest request, HttpServletResponse response, Exception e) {
        AjaxResponse err = new AjaxResponse();

        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String query = request.getQueryString();
        if (StringUtils.isNotEmpty(query)) {
            url += "?" + query;
        }

        Map<String, String> headers = WebUtils.getHeaders(request);
        Map<String, String> params = WebUtils.getParameterMap(request);
        String bodyString = "";
        try {
            bodyString = WebUtils.getStreamAsString(request.getInputStream(), "UTF-8");
        } catch (IOException e1) {
        }

        log.error("系统异常:{} {}\nheader:{}\nparameter:{}\nbody:{}",
                method, url, JSON.toJSONString(headers), JSON.toJSONString(params), bodyString, e);

        if (e instanceof DaoException) {
            DaoException daoException = (DaoException) e;
            err.putError(CommonErrorCode.BIZ_FAIL.getCode(), daoException.getErrorMessage());
        } else {
            err.putError(CommonErrorCode.SYSTEM_ERROR.getCode(), "执行失败，请联系管理员");
        }

        return err.toJSONString();
    }
}
