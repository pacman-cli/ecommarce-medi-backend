package com.example.ecommerce.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OTP / email-verification configuration bound from the {@code app.verification.*} namespace.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.verification")
public class VerificationProperties {

    /** Number of digits in a generated one-time code. */
    private int otpLength = 6;

    /** Lifetime of an issued code in minutes. */
    private int otpTtlMinutes = 10;

    /** Maximum verification attempts per code before it is invalidated. */
    private int maxAttempts = 5;

    /** When {@code true}, login is rejected for accounts whose email is not yet verified. */
    private boolean requireVerifiedLogin = true;
}