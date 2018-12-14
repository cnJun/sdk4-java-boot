package com.sdk4.boot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

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

    @Before(value = "pointcutConfig()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        getLogInfo(joinPoint);
    }

    @AfterReturning(returning = "retValue", pointcut = "pointcutConfig()")
    public void doAfterReturning(Object retValue) {
    }

    private void getLogInfo(JoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Method method = sign.getMethod();
        String service = method.getDeclaringClass().getName();
        String methodName = method.getName();
        System.out.println(service + "#" + methodName + "#" + (args == null ? "" : args.length));
    }
}
