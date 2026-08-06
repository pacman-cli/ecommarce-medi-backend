package com.example.ecommerce.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT configuration properties bound from the {@code app.jwt.*} namespace.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /**
     * HMAC secret used to sign tokens. Must be at least 256 bits (32 bytes).
     * Always override in production via the {@code JWT_SECRET} environment variable.
     */
    private String secret = "change-me-please-change-me-please-secure-jwt-secret-0123456789";

    /** Access token lifetime in milliseconds. Default: 15 minutes. */
    private long accessTokenExpirationMs = 900_000L;

    /** Refresh token lifetime in milliseconds. Default: 7 days. */
    private long refreshTokenExpirationMs = 604_800_000L;

    /** Issuer claim written into every token. */
    private String issuer = "ecommerce-api";
}
