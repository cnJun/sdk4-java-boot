package com.sdk4.boot.aop;

import com.sdk4.boot.trace.Eye;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 执行监控
 *
 * @author sh
 */
@Slf4j
@Aspect
@Component
public class ProfileAspect {

    @Pointcut("execution(public * com..controller..*(..)) || " +
            "execution(public * com..service..*(..)) || " +
            "execution(public * com..mapper..*(..)) || " +
            "execution(public * com..repository..*(..))")
    public void pointcutConfig(){
    }

    @Before(value = "pointcutConfig()")
    public void doBefore(JoinPoint joinPoint) {
        Point point = getPoint(joinPoint);
        Eye.startLocal(point.service, point.method);
    }

    @AfterReturning(value = "pointcutConfig()", returning = "retValue")
    public void doAfterReturning(JoinPoint joinPoint, Object retValue) {
        Eye.endLocal();
    }

    @AfterThrowing(value = "pointcutConfig()", throwing = "e")
    public void doThrowing(JoinPoint joinPoint, Exception e) {
        Eye.endLocal();
    }

    private Point getPoint(JoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();

        Point point = new Point();

        point.service = method.getDeclaringClass().getName();
        point.method = method.getName();

        return point;
    }

    static class Point {
        String service;
        String method;
    }

}
