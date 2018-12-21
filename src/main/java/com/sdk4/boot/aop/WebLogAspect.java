package com.sdk4.boot.aop;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sdk4.boot.common.BaseRequest;
import com.sdk4.common.util.WebUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author sh
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {
    @Pointcut("execution(public * com..controller..*(..))")
    public void pointcutConfig(){
    }

    @AfterReturning(value = "pointcutConfig()", returning = "retValue")
    public void doAfterReturning(JoinPoint joinPoint, Object retValue) {
        WebLogInfo webLogInfo = getLogInfo(joinPoint);
        webLogInfo.returnString = retValue == null ? "" : JSON.toJSONString(retValue);
        if (log.isDebugEnabled()) {
            log.debug("请求响应:{}:{}", webLogInfo.getUrl(), JSON.toJSONString(webLogInfo));
        }
    }

    @AfterThrowing(value = "pointcutConfig()", throwing = "e")
    public void doThrowing(JoinPoint joinPoint, Exception e) {
        // ErrorHandler 中已经做了异常捕获，这里不需要重复进行
        // WebLogInfo webLogInfo = getLogInfo(joinPoint);
        // log.error("请求异常:{}:{}", webLogInfo.getUrl(), JSON.toJSONString(webLogInfo), e);
    }

    private WebLogInfo getLogInfo(JoinPoint joinPoint) {
        WebLogInfo webLogInfo = new WebLogInfo();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        webLogInfo.type = request.getMethod();
        webLogInfo.url = request.getRequestURL().toString();
        webLogInfo.queryString = request.getQueryString();
        webLogInfo.headers = WebUtils.getHeaders(request);

        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Method method = sign.getMethod();

        webLogInfo.service = method.getDeclaringClass().getName();
        webLogInfo.method = method.getName();
        webLogInfo.params = Maps.newHashMap();

        int paramIndex = 0;

        for (Object arg : args) {
            paramIndex++;

            if (arg instanceof Map || arg instanceof Number || arg instanceof String) {
                webLogInfo.params.put(paramIndex, arg);
            } else if (arg instanceof BaseRequest) {
                webLogInfo.params.put(paramIndex, arg);
            }
        }

        return webLogInfo;
    }

    @Data
    public static class WebLogInfo {
        private String type;
        private String url;
        private String queryString;
        private Map<String, String> headers;
        private String service;
        private String method;
        private Map<Integer, Object> params;
        private String returnString;
    }
}
