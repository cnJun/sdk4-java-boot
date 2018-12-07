package com.sdk4.boot.filter;

import com.alibaba.fastjson.JSON;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.exception.BaseError;
import com.sdk4.boot.exception.BusinessException;
import com.sdk4.common.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 错误拦截
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(value = BusinessException.class)
    public BaseResponse handlerBusinessException(HttpServletRequest request, BusinessException e) {
        logError(request, e);
        return new BaseResponse(e);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public BaseResponse handlerMaxUploadSizeExceededException(HttpServletRequest request, MaxUploadSizeExceededException e) {
        logError(request, e);
        return new BaseResponse(BaseError.INVALID_PARAMETER.getCode(), "文件大小不能超过" + maxFileSize);
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse handlerException(HttpServletRequest request,  Exception e) {
        logError(request, e);
        return new BaseResponse(BaseError.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public BaseResponse handlerRuntimeException(HttpServletRequest request, RuntimeException e) {
        logError(request, e);
        return new BaseResponse(BaseError.SYSTEM_ERROR);
    }

    private void logError(HttpServletRequest request, Exception e) {
        if (request == null) {
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
                log.error("获取请求body流异常", e1);
            }

            log.error("系统异常:{} {}\nheader:{}\nparameter:{}\nbody:{}",
                    method, url, JSON.toJSONString(headers), JSON.toJSONString(params), bodyString, e);
        } else {
            log.error("系统异常", e);
        }
    }
}
