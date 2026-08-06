package com.example.ecommerce.exception;

/**
 * Exception thrown when authenticated user lacks authorization permissions for requested resource.
 */
public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(ErrorCode.FORBIDDEN, message);
    }
}
