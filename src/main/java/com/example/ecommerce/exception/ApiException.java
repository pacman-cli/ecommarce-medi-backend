package com.example.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for all domain-specific business exceptions.
 */
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Creates an exception with a message and HTTP status.
     *
     * @param status  the HTTP status mapped by the global handler
     * @param message the error message
     */
    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * Creates an exception with a message, HTTP status and root cause.
     *
     * @param status  the HTTP status mapped by the global handler
     * @param message the error message
     * @param cause   the underlying cause
     */
    public ApiException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
