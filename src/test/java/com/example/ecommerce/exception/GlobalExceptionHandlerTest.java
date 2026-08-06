package com.example.ecommerce.exception;

import com.example.ecommerce.common.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", "id", 200L);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBaseException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("ERR_RESOURCE_NOT_FOUND", response.getBody().getErrorCode());
        assertTrue(response.getBody().getMessage().contains("Product not found with id : '200'"));
    }

    @Test
    void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Order state transition not allowed");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBaseException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("ERR_BUSINESS_RULE_VIOLATION", response.getBody().getErrorCode());
        assertEquals("Order state transition not allowed", response.getBody().getMessage());
    }

    @Test
    void testHandleCustomValidationException() {
        Map<String, String> fieldErrors = Collections.singletonMap("email", "Invalid email format");
        CustomValidationException ex = new CustomValidationException("Validation failed", fieldErrors);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBaseException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERR_VALIDATION_FAILED", response.getBody().getErrorCode());
        assertEquals(fieldErrors, response.getBody().getFieldErrors());
    }

    @Test
    void testHandleAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Access is denied");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDenied(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("ERR_FORBIDDEN", response.getBody().getErrorCode());
    }

    @Test
    void testHandleGenericException() {
        NullPointerException ex = new NullPointerException("Null pointer reference");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneric(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERR_INTERNAL_SERVER_ERROR", response.getBody().getErrorCode());
    }
}
