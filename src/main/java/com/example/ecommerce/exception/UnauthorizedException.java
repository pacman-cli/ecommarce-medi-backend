package com.example.ecommerce.exception;

/**
 * Exception thrown when authentication fails or is missing.
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}
