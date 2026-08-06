package com.example.ecommerce.exception;

import lombok.Getter;

/**
 * Root abstract custom runtime exception carrying an explicit {@link ErrorCode}.
 */
@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BaseException(ErrorCode errorCode, String message) {
        super(message != null ? message : errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    protected BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message != null ? message : errorCode.getDefaultMessage(), cause);
        this.errorCode = errorCode;
    }
}
