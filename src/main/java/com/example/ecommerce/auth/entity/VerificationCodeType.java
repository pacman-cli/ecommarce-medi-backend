package com.example.ecommerce.auth.entity;

/**
 * The purpose of a persisted one-time verification code.
 */
public enum VerificationCodeType {

    /** Code used to verify ownership of an email address. */
    EMAIL_VERIFICATION,

    /** Code used to authorize a password reset. */
    PASSWORD_RESET
}