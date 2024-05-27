package com.gzx.hotel.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.gzx.hotel.base.controller..*(..))")
    private void baseController() {}

    @Pointcut("execution(* com.gzx.hotel.core.controller..*(..))")
    private void coreController() {}
    
    @Before("baseController() || coreController()")
    public void logBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录请求信息
        log.info("请求URL : " + request.getRequestURL().toString());
        log.info("IP地址 : " + request.getRemoteAddr());
        log.info("方法签名 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("方法参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "baseController() || coreController()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // 记录响应信息
        log.info("响应 : " + result);
    }
}