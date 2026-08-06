package com.example.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Production-ready OpenAPI 3 / Swagger documentation configuration exposing API groups,
 * JWT authentication security schemes, reusable error response examples, pagination,
 * dynamic multi-criteria filtering parameter guidelines, and server metadata.
 */
@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * Group 1: Public Endpoints (Authentication, Product Catalog, Search, Categories, Brands, Reviews, Wishlist).
     */
    @Bean
    public GroupedOpenApi publicApiGroup() {
        return GroupedOpenApi.builder()
                .group("1. Public APIs")
                .pathsToMatch(
                        "/api/v1/auth/**",
                        "/api/v1/products/**",
                        "/api/v1/categories/**",
                        "/api/v1/brands/**",
                        "/api/v1/reviews/**",
                        "/api/v1/wishlist/**",
                        "/api/v1/search/**"
                )
                .build();
    }

    /**
     * Group 2: Customer Endpoints (Profile, Addresses, Cart, Orders, Payments, Delivery Tracking, Notifications).
     */
    @Bean
    public GroupedOpenApi customerApiGroup() {
        return GroupedOpenApi.builder()
                .group("2. Customer APIs")
                .pathsToMatch(
                        "/api/v1/users/**",
                        "/api/v1/addresses/**",
                        "/api/v1/cart/**",
                        "/api/v1/orders/**",
                        "/api/v1/payments/**",
                        "/api/v1/delivery/**",
                        "/api/v1/notifications/**"
                )
                .build();
    }

    /**
     * Group 3: Admin & Operational Endpoints (Suppliers, Purchases, Inventory, Coupons, Audit, Logging, Cache, Email, Scheduler, Dashboard).
     */
    @Bean
    public GroupedOpenApi adminApiGroup() {
        return GroupedOpenApi.builder()
                .group("3. Admin & Operational APIs")
                .pathsToMatch(
                        "/api/v1/suppliers/**",
                        "/api/v1/purchases/**",
                        "/api/v1/inventory/**",
                        "/api/v1/coupons/**",
                        "/api/v1/audit/**",
                        "/api/v1/logging/**",
                        "/api/v1/cache/**",
                        "/api/v1/email/**",
                        "/api/v1/scheduler/**",
                        "/api/v1/dashboard/**"
                )
                .build();
    }

    /**
     * Group 4: All v1 APIs.
     */
    @Bean
    public GroupedOpenApi allApiGroup() {
        return GroupedOpenApi.builder()
                .group("4. All v1 APIs")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    /**
     * Builds the main OpenAPI document metadata, security scheme, servers, and reusable response components.
     *
     * @return the fully configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI ecommerceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Platform REST API")
                        .description("### Production-Ready Enterprise E-Commerce Engine\n\n"
                                + "Provides RESTful endpoints for multi-vendor catalog management, inventory stock tracking, "
                                + "order checkout, payment processing, delivery logistics, supplier management, and system auditing.\n\n"
                                + "#### Authentication\n"
                                + "Submit credentials to `/api/v1/auth/login` to obtain an access token. Include the returned "
                                + "token in the HTTP request header: `Authorization: Bearer <your_jwt_token>`.\n\n"
                                + "#### Standard Response Envelope\n"
                                + "All responses are wrapped in a standard `ApiResponse<T>` envelope containing `success`, `message`, `data`, `status`, `traceId`, and `timestamp`.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("E-Commerce Architecture Team")
                                .email("architecture@example.com")
                                .url("https://example.com/api/support"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("/").description("Current Host Environment"),
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(createComponents());
    }

    /**
     * Creates reusable OpenAPI components for JWT SecurityScheme and standard HTTP error response examples.
     */
    private Components createComponents() {
        Components components = new Components();

        // 1. JWT Bearer Security Scheme
        components.addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter your JWT access token obtained from /api/v1/auth/login. Prefix with 'Bearer '."));

        // 2. Reusable Examples
        Example badRequestExample = new Example().value("""
                {
                  "success": false,
                  "message": "Invalid request parameter or payload",
                  "data": null,
                  "status": 400,
                  "traceId": "REQ-8F12A0C9",
                  "timestamp": "2026-08-05T22:27:00Z",
                  "error": {
                    "code": "ERR_BAD_REQUEST",
                    "message": "Request payload validation failed",
                    "status": 400,
                    "timestamp": "2026-08-05T22:27:00Z"
                  }
                }
                """);

        Example unauthorizedExample = new Example().value("""
                {
                  "success": false,
                  "message": "Full authentication is required to access this resource",
                  "data": null,
                  "status": 401,
                  "traceId": "REQ-7C91B4D2",
                  "timestamp": "2026-08-05T22:27:00Z",
                  "error": {
                    "code": "ERR_UNAUTHORIZED",
                    "message": "JWT token is missing, expired or invalid",
                    "status": 401,
                    "timestamp": "2026-08-05T22:27:00Z"
                  }
                }
                """);

        Example forbiddenExample = new Example().value("""
                {
                  "success": false,
                  "message": "Access denied for current user roles",
                  "data": null,
                  "status": 403,
                  "traceId": "REQ-3E41F90A",
                  "timestamp": "2026-08-05T22:27:00Z",
                  "error": {
                    "code": "ERR_FORBIDDEN",
                    "message": "User does not possess authority ROLE_ADMIN",
                    "status": 403,
                    "timestamp": "2026-08-05T22:27:00Z"
                  }
                }
                """);

        Example notFoundExample = new Example().value("""
                {
                  "success": false,
                  "message": "Requested resource not found",
                  "data": null,
                  "status": 404,
                  "traceId": "REQ-1A2B3C4D",
                  "timestamp": "2026-08-05T22:27:00Z",
                  "error": {
                    "code": "ERR_RESOURCE_NOT_FOUND",
                    "message": "Product not found with id: '999'",
                    "status": 404,
                    "timestamp": "2026-08-05T22:27:00Z"
                  }
                }
                """);

        Example conflictExample = new Example().value("""
                {
                  "success": false,
                  "message": "Unique constraint violation",
                  "data": null,
                  "status": 409,
                  "traceId": "REQ-90E8D7C6",
                  "timestamp": "2026-08-05T22:27:00Z",
                  "error": {
                    "code": "ERR_CONFLICT",
                    "message": "Product with SKU 'MED-PARA-500' already exists",
                    "status": 409,
                    "timestamp": "2026-08-05T22:27:00Z"
                  }
                }
                """);

        Example validationErrorExample = new Example().value("""
                {
                  "success": false,
                  "message": "Validation failed",
                  "data": null,
                  "status": 422,
                  "traceId": "REQ-55A43210",
                  "timestamp": "2026-08-05T22:27:00Z",
                  "error": {
                    "code": "ERR_VALIDATION_FAILED",
                    "message": "Field validation errors occurred",
                    "status": 422,
                    "timestamp": "2026-08-05T22:27:00Z",
                    "fieldErrors": [
                      {
                        "field": "email",
                        "rejectedValue": "invalid-email-address",
                        "message": "Email address must be well-formed"
                      },
                      {
                        "field": "sellingPrice",
                        "rejectedValue": "-10.00",
                        "message": "Selling price must be greater than zero"
                      }
                    ]
                  }
                }
                """);

        Example internalServerErrorExample = new Example().value("""
                {
                  "success": false,
                  "message": "An unexpected server error occurred",
                  "data": null,
                  "status": 500,
                  "traceId": "REQ-FFFF9999",
                  "timestamp": "2026-08-05T22:27:00Z",
                  "error": {
                    "code": "ERR_INTERNAL_SERVER_ERROR",
                    "message": "Database transaction failure",
                    "status": 500,
                    "timestamp": "2026-08-05T22:27:00Z"
                  }
                }
                """);

        components.addExamples("BadRequestExample", badRequestExample);
        components.addExamples("UnauthorizedExample", unauthorizedExample);
        components.addExamples("ForbiddenExample", forbiddenExample);
        components.addExamples("NotFoundExample", notFoundExample);
        components.addExamples("ConflictExample", conflictExample);
        components.addExamples("ValidationErrorExample", validationErrorExample);
        components.addExamples("InternalServerErrorExample", internalServerErrorExample);

        // 3. Reusable ApiResponses
        components.addResponses("400BadRequest", new ApiResponse()
                .description("Bad Request - Malformed parameter or request payload")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(badRequestExample.getValue()))));

        components.addResponses("401Unauthorized", new ApiResponse()
                .description("Unauthorized - Authentication token missing or expired")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(unauthorizedExample.getValue()))));

        components.addResponses("403Forbidden", new ApiResponse()
                .description("Forbidden - Insufficient privileges")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(forbiddenExample.getValue()))));

        components.addResponses("404NotFound", new ApiResponse()
                .description("Not Found - Requested entity does not exist")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(notFoundExample.getValue()))));

        components.addResponses("409Conflict", new ApiResponse()
                .description("Conflict - Unique business rule or constraint violation")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(conflictExample.getValue()))));

        components.addResponses("422ValidationError", new ApiResponse()
                .description("Unprocessable Entity - Input validation failure")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(validationErrorExample.getValue()))));

        components.addResponses("500InternalServerError", new ApiResponse()
                .description("Internal Server Error - Unexpected server fault")
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(internalServerErrorExample.getValue()))));

        return components;
    }

    /**
     * Operation customizer adding response guidelines to API endpoints.
     */
    @Bean
    public OperationCustomizer globalOperationCustomizer() {
        return (operation, handlerMethod) -> {
            if (operation.getResponses() != null) {
                if (!operation.getResponses().containsKey("500")) {
                    operation.getResponses().addApiResponse("500", new ApiResponse().$ref("#/components/responses/500InternalServerError"));
                }
            }
            return operation;
        };
    }
}
