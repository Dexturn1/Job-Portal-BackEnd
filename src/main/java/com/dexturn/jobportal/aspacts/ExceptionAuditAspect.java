package com.dexturn.jobportal.aspacts;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@Aspect
public class ExceptionAuditAspect {

    @AfterThrowing(
            pointcut = "execption(* com.dexturn..jobportal..*.*(..))",
            throwing = "ex"
    )
    public void logAfterException(JoinPoint joinPoint, Exception ex){
        String methodName = joinPoint.getSignature().toShortString();
        Object[] methodArgs = joinPoint.getArgs();

        log.error("❌ Exception occurred in method: {}", methodName);
        log.error("📥 Arguments:{}", Arrays.toString(methodArgs));
        log.error("💥 Exception type:{}", ex.getClass().getSimpleName());
        log.error("📨 Exception message:{}", ex.getMessage());
    }
}
