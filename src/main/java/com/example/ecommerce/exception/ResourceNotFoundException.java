package com.example.ecommerce.exception;

/**
 * Exception thrown when a requested domain entity resource cannot be found in persistence.
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(ErrorCode.RESOURCE_NOT_FOUND, String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message, cause);
    }
}
