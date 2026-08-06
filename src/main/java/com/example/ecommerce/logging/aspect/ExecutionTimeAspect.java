package com.example.ecommerce.logging.aspect;

import com.example.ecommerce.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AOP Aspect intercepting methods annotated with {@link LogExecutionTime}
 * to measure, profile, and log method execution latency in milliseconds.
 */
@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(com.example.ecommerce.logging.annotation.LogExecutionTime) || @within(com.example.ecommerce.logging.annotation.LogExecutionTime)")
    public Object profileMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogExecutionTime annotation = method.getAnnotation(LogExecutionTime.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(LogExecutionTime.class);
        }

        long warnThresholdMs = annotation != null ? annotation.warnThresholdMs() : 500L;

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            String className = signature.getDeclaringType().getSimpleName();
            String methodName = signature.getName();

            if (duration >= warnThresholdMs) {
                log.warn("SLOW EXECUTION DETECTED: {}.{}() executed in {} ms (Threshold: {} ms)",
                        className, methodName, duration, warnThresholdMs);
            } else {
                log.info("{}.{}() executed in {} ms", className, methodName, duration);
            }
        }
    }
}
