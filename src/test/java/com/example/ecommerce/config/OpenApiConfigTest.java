package com.example.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private OpenApiConfig openApiConfig;

    @BeforeEach
    void setUp() {
        openApiConfig = new OpenApiConfig();
    }

    @Test
    void testOpenApiConfigurationBean() {
        OpenAPI openAPI = openApiConfig.ecommerceOpenApi();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("E-Commerce Platform REST API", openAPI.getInfo().getTitle());
        assertEquals("v1.0.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getComponents());

        SecurityScheme bearerScheme = openAPI.getComponents().getSecuritySchemes().get(OpenApiConfig.SECURITY_SCHEME_NAME);
        assertNotNull(bearerScheme);
        assertEquals(SecurityScheme.Type.HTTP, bearerScheme.getType());
        assertEquals("bearer", bearerScheme.getScheme());
        assertEquals("JWT", bearerScheme.getBearerFormat());
    }

    @Test
    void testGroupedApiBeans() {
        GroupedOpenApi publicGroup = openApiConfig.publicApiGroup();
        assertNotNull(publicGroup);
        assertEquals("1. Public APIs", publicGroup.getGroup());

        GroupedOpenApi customerGroup = openApiConfig.customerApiGroup();
        assertNotNull(customerGroup);
        assertEquals("2. Customer APIs", customerGroup.getGroup());

        GroupedOpenApi adminGroup = openApiConfig.adminApiGroup();
        assertNotNull(adminGroup);
        assertEquals("3. Admin & Operational APIs", adminGroup.getGroup());

        GroupedOpenApi allGroup = openApiConfig.allApiGroup();
        assertNotNull(allGroup);
        assertEquals("4. All v1 APIs", allGroup.getGroup());
    }
}
