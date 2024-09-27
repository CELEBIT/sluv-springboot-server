package com.sluv.api.common.log;

import jakarta.servlet.http.HttpServletRequest;
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

@Slf4j
@Aspect
@Component
public class RestControllerLogAop {

    @Pointcut("execution(* com.sluv.api.*.controller..*.*(..))")
    private void cut() {
    }

    // Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {

        // Request URI
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            // Log the requested URL
            log.info("====== Requested URL: {} ======", request.getRequestURI());
        }

        if (requestAttributes == null) {
            log.info("====== Requested URL: Unknown ======");
        }

        // 파라미터 받아오기
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();


        if (args.length <= 0) {
            log.info("Request Data: null");
        } else {
            for (int i = 0; i < parameterNames.length; i++) {
                String paramName = parameterNames[i];
                String paramType = args[i] != null ? args[i].getClass().getSimpleName() : "null";
                Object paramValue = args[i];
                log.info("Request Data [name: {}, type: {}, value: {}]", paramName, paramType, paramValue);
            }
        }

    }

    // Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {

        // Request URI
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            // Log the requested URL
            log.info("Requested URL: {}", request.getRequestURI());
        }
        if (requestAttributes == null) {
            log.info("====== Requested URL: Unknown ======");
        }

        log.info("Return: type = {}, Value = {}", returnObj.getClass().getSimpleName(), returnObj);
    }

}