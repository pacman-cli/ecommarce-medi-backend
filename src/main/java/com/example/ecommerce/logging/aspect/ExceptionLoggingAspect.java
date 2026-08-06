package com.example.ecommerce.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect intercepting and logging unhandled exceptions thrown across application controllers and services.
 */
@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @Pointcut("within(com.example.ecommerce..*) && !within(com.example.ecommerce.security..*)")
    public void applicationPackagePointcut() {
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        log.error("EXCEPTION THROWN in {}.{}() with message: {}",
                className, methodName, e.getMessage(), e);
    }
}
