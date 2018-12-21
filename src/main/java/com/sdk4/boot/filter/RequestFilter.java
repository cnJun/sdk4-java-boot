package com.sdk4.boot.filter;

import com.sdk4.boot.trace.Tracer;
import com.sdk4.common.id.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求过滤
 *
 * @author sh
 */
@Slf4j
public class RequestFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = IdUtils.fastUUID().toString();
        Tracer.start(traceId, request, response);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        long cost = Tracer.getCost();
        String stackLog = Tracer.end(cost > 200 || log.isDebugEnabled());
        if (StringUtils.isNotEmpty(stackLog)) {
            log.info(stackLog);
        }
    }
}
