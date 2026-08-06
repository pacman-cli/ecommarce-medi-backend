package com.example.ecommerce.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * CORS configuration properties bound from the {@code app.cors.*} namespace.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /**
     * Allowed browser origins. Must be explicit origins (not {@code "*"}) when
     * credentials are enabled.
     */
    private List<String> allowedOrigins = new ArrayList<>(List.of("http://localhost:3000"));
}
