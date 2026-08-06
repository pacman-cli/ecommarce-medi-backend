package com.example.ecommerce.common.email;

/**
 * The business purpose of an emailed one-time code, used to render the correct
 * subject line and body.
 */
public enum VerificationPurpose {

    /** Confirming possession of the email address. */
    EMAIL_VERIFICATION,

    /** Enabling a password reset. */
    PASSWORD_RESET
}