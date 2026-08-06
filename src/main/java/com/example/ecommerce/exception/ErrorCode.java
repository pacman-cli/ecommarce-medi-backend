package com.example.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Standardized machine-readable error codes bound to HTTP status codes and default descriptions.
 */
@Getter
public enum ErrorCode {

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR_RESOURCE_NOT_FOUND", "Requested resource was not found"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "ERR_DUPLICATE_RESOURCE", "Resource with given identifier already exists"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "ERR_VALIDATION_FAILED", "Request validation failed"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ERR_UNAUTHORIZED", "Authentication required or invalid credentials"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "ERR_FORBIDDEN", "Access denied for requested action"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "ERR_BAD_REQUEST", "Invalid request parameters or payload"),
    BUSINESS_RULE_VIOLATION(HttpStatus.UNPROCESSABLE_ENTITY, "ERR_BUSINESS_RULE_VIOLATION", "Business rule constraint violated"),
    INSUFFICIENT_STOCK(HttpStatus.UNPROCESSABLE_ENTITY, "ERR_INSUFFICIENT_STOCK", "Insufficient inventory stock available"),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "ERR_PAYMENT_FAILED", "Payment processing transaction failed"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "ERR_METHOD_NOT_ALLOWED", "HTTP method not supported for this endpoint"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERR_INTERNAL_SERVER_ERROR", "An unexpected internal server error occurred");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String code, String defaultMessage) {
        this.status = status;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
