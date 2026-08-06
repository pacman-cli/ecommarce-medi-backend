package com.example.ecommerce.exception;

/**
 * Exception thrown when a resource creation or update conflicts with existing state (e.g. duplicate unique code/email).
 */
public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(ErrorCode.DUPLICATE_RESOURCE, message);
    }
}
