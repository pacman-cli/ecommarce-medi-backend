package com.example.ecommerce.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis application key-prefix properties bound from the {@code app.redis.*} namespace.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.redis")
public class RedisProperties {

    /**
     * Namespace prefix prepended to every application-managed Redis key so the
     * instance can be shared safely between services.
     */
    private String keyPrefix = "ecommerce";
}
