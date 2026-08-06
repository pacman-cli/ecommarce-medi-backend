package com.example.ecommerce.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Abstract base class for Spring Boot integration tests providing Testcontainers integration
 * with automated H2 fallback if Docker daemon is not active.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    protected static PostgreSQLContainer<?> postgres;

    static {
        try {
            postgres = new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("ecommerce_test")
                    .withUsername("test")
                    .withPassword("test");
            postgres.start();
        } catch (Exception e) {
            // Docker daemon not active on host machine: test context falls back to application-test.yml (H2 PostgreSQL mode)
            postgres = null;
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if (postgres != null && postgres.isRunning()) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
            registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        }
    }
}
