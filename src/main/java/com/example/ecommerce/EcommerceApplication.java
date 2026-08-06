package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Entry point of the e-commerce backend application.
 *
 * <p>{@code @ConfigurationPropertiesScan} picks up all {@code @ConfigurationProperties}
 * classes (e.g. {@code JwtProperties}, {@code CorsProperties}) from the base package.</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class EcommerceApplication {

    /**
     * Bootstraps the Spring application context.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
