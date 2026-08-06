package com.example.ecommerce.exception;

/**
 * Exception thrown when business logic constraints or domain workflow rules fail.
 */
public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, message);
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
