package com.example.ecommerce.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Brute-force protection configuration bound from the {@code app.security.*} namespace.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    /** Failures allowed within the lockout window before the account is blocked. */
    private int maxFailedLoginAttempts = 5;

    /** How long an account stays blocked after exceeding the failed-attempt threshold. */
    private int lockoutDurationMinutes = 15;
}