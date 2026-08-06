package com.example.ecommerce.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a user attempts to log in before verifying their email address.
 * Maps to {@code 403 Forbidden}.
 */
public class EmailNotVerifiedException extends ApiException {

    /**
     * Creates the exception.
     *
     * @param message the error message
     */
    public EmailNotVerifiedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}