package com.example.ecommerce.exception;

/**
 * Exception thrown when a client request is invalid or malformed.
 */
public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(ErrorCode.BAD_REQUEST, message, cause);
    }
}
