package com.example.ecommerce.logging.aspect;

import com.example.ecommerce.logging.annotation.LogExecutionTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionTimeAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @InjectMocks
    private ExecutionTimeAspect executionTimeAspect;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        Method dummyMethod = SampleDummyClass.class.getMethod("sampleMethod");
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(dummyMethod);
        when(methodSignature.getDeclaringType()).thenReturn((Class) SampleDummyClass.class);
        when(methodSignature.getName()).thenReturn("sampleMethod");
    }

    @Test
    void testProfileMethodExecutionSuccess() throws Throwable {
        when(joinPoint.proceed()).thenReturn("SUCCESS_RESULT");

        Object result = executionTimeAspect.profileMethodExecution(joinPoint);

        assertEquals("SUCCESS_RESULT", result);
        verify(joinPoint, times(1)).proceed();
    }

    static class SampleDummyClass {
        @LogExecutionTime(warnThresholdMs = 200L)
        public String sampleMethod() {
            return "SUCCESS_RESULT";
        }
    }
}
