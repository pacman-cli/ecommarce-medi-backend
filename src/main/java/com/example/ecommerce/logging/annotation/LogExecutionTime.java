package com.example.ecommerce.logging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to measure and log method execution duration in milliseconds via AOP.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

    /**
     * Threshold in milliseconds above which execution is logged as a WARN.
     */
    long warnThresholdMs() default 500L;
}
