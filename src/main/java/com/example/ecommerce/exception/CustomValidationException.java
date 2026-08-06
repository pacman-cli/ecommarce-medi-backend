package com.example.ecommerce.exception;

import lombok.Getter;

import java.util.Map;

/**
 * Exception thrown for custom programmatic validation failures carrying field-level error messages map.
 */
@Getter
public class CustomValidationException extends BaseException {

    private final Map<String, String> fieldErrors;

    public CustomValidationException(String message, Map<String, String> fieldErrors) {
        super(ErrorCode.VALIDATION_FAILED, message);
        this.fieldErrors = fieldErrors;
    }
}
