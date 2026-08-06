package com.example.ecommerce.exception;

import com.example.ecommerce.common.constant.AppConstants;
import com.example.ecommerce.common.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Central exception handler translating all domain, framework, validation, security,
 * and database failures into standardized {@link ErrorResponse} contracts with machine-readable {@link ErrorCode}s.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /* ---------------------------- Custom Base & Domain Exceptions ---------------------------- */

    /**
     * Handles custom domain exceptions deriving from {@link BaseException}.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        log.warn("Domain exception [{}]: {}", ex.getErrorCode().getCode(), ex.getMessage());
        Map<String, String> fieldErrors = null;
        if (ex instanceof CustomValidationException customValEx) {
            fieldErrors = customValEx.getFieldErrors();
        }
        return buildResponse(ex.getErrorCode().getStatus(), ex.getErrorCode(), ex.getMessage(), request, fieldErrors);
    }

    /**
     * Handles Spring Security authorization access denials (403 Forbidden).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied on URI {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, "You do not have permission to perform this action", request, null);
    }

    /**
     * Handles Spring Security authentication failures (401 Unauthorized).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.warn("Authentication failed on URI {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "Authentication failed: " + ex.getMessage(), request, null);
    }

    /* -------------------------- Validation Exceptions -------------------------- */

    /**
     * Handles {@code @Valid} request-body failures with field-level messages (400 Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        log.warn("Validation failed for URI {}: {} field errors", request.getRequestURI(), fieldErrors.size());
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, "Request validation failed", request, fieldErrors);
    }

    /**
     * Handles constraint violations raised outside controller method arguments (400 Bad Request).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            fieldErrors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, "Constraint validation failed", request, fieldErrors);
    }

    /* --------------------------- Framework Exceptions -------------------------- */

    /**
     * Handles file upload size exceeding limits (400 Bad Request).
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "File upload size exceeds maximum allowed limit", request, null);
    }

    /**
     * Handles malformed JSON request bodies (400 Bad Request).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "Malformed or unparseable JSON request body", request, null);
    }

    /**
     * Handles wrong-typed path/query parameters (400 Bad Request).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "Invalid value for parameter: " + ex.getName(), request, null);
    }

    /**
     * Handles missing required query parameters (400 Bad Request).
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "Missing required parameter: " + ex.getParameterName(), request, null);
    }

    /**
     * Handles unsupported HTTP methods (405 Method Not Allowed).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED, "HTTP method '" + ex.getMethod() + "' not supported for this endpoint", request, null);
    }

    /**
     * Handles unmatched static-resource/route lookups (404 Not Found).
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, "No endpoint resource found for path: " + request.getRequestURI(), request, null);
    }

    /* --------------------------- Data Integrity Failures ------------------------ */

    /**
     * Handles unique-constraint violations surfaced by database (409 Conflict).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Database data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return buildResponse(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_RESOURCE, "Database constraint conflict: operation conflicts with existing data", request, null);
    }

    /* ------------------------------ System Catch-All ------------------------------ */

    /**
     * Handles unexpected system exceptions hiding internal stack traces from clients (500 Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled system exception on URI {}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected internal error occurred. Please contact system support.", request, null);
    }

    /**
     * Assembles the standardized {@link ErrorResponse} payload.
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, ErrorCode errorCode, String message, HttpServletRequest request, Map<String, String> fieldErrors) {
        String traceId = MDC.get(AppConstants.TRACE_ID_MDC_KEY);
        if (traceId == null) {
            traceId = MDC.get("requestId");
        }

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .errorCode(errorCode != null ? errorCode.getCode() : "ERR_GENERIC")
                .message(message)
                .path(request != null ? request.getRequestURI() : null)
                .traceId(traceId)
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
