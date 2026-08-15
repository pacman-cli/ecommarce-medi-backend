package com.example.ecommerce.config;

import com.example.ecommerce.config.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Global CORS configuration.
 *
 * <p>The same property-driven configuration is exposed in two ways:</p>
 * <ul>
 *     <li>{@link WebMvcConfigurer} for framework-level (non-security) requests.</li>
 *     <li>A {@link CorsConfigurationSource} bean consumed by the Spring Security
 *         filter chain, otherwise CORS pre-flight would be rejected by security.</li>
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

    private final CorsProperties corsProperties;

    /**
     * Registers global CORS mappings for Spring MVC.
     *
     * @param registry the CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = corsProperties.getAllowedOrigins();
        boolean hasWildcard = origins.stream().anyMatch(o -> o.contains("*"));

        if (hasWildcard) {
            registry.addMapping("/**")
                    .allowedOriginPatterns(origins.toArray(String[]::new))
                    .allowedMethods(ALLOWED_METHODS.toArray(String[]::new))
                    .allowedHeaders("*")
                    .exposedHeaders("X-Trace-Id")
                    .allowCredentials(true)
                    .maxAge(3600);
        } else {
            registry.addMapping("/**")
                    .allowedOrigins(origins.toArray(String[]::new))
                    .allowedMethods(ALLOWED_METHODS.toArray(String[]::new))
                    .allowedHeaders("*")
                    .exposedHeaders("X-Trace-Id")
                    .allowCredentials(true)
                    .maxAge(3600);
        }
    }

    /**
     * Exposes the same CORS policy to the Spring Security filter chain.
     *
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = corsProperties.getAllowedOrigins();
        boolean hasWildcard = origins.stream().anyMatch(o -> o.contains("*"));

        if (hasWildcard) {
            configuration.setAllowedOriginPatterns(origins);
        } else {
            configuration.setAllowedOrigins(origins);
        }

        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("X-Trace-Id"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
