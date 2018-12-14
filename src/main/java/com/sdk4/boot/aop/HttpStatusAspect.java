package com.sdk4.boot.aop;

import com.sdk4.boot.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author sh
 */
@Slf4j
@Aspect
@Component
public class HttpStatusAspect {
    @Pointcut("execution(public * com..controller..*(..))")
    public void pointcutConfig(){
    }

    @AfterReturning(pointcut = "pointcutConfig()", returning = "retValue")
    public void doAfterReturning(Object retValue) throws Throwable {
        if (retValue instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse) retValue;
            if (!baseResponse.isSuccess()) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                attributes.getResponse().setStatus(baseResponse.getHttpStatus().value());
            }
        }
    }
}
