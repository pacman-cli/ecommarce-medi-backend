package com.example.ecommerce.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for adding an item to the shopping cart.
 */
@Schema(description = "Payload for adding an item to shopping cart")
public class AddToCartRequest {

    @NotNull(message = "Product ID is required")
    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Quantity to add", example = "2")
    private Integer quantity;

    @Size(max = 100, message = "Session ID must not exceed 100 characters")
    @Schema(description = "Guest session ID (required if unauthenticated)", example = "guest-session-12345")
    private String sessionId;

    public AddToCartRequest() {
    }

    public AddToCartRequest(Long productId, Integer quantity, String sessionId) {
        this.productId = productId;
        this.quantity = quantity;
        this.sessionId = sessionId;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public static AddToCartRequestBuilder builder() { return new AddToCartRequestBuilder(); }

    public static class AddToCartRequestBuilder {
        private Long productId;
        private Integer quantity;
        private String sessionId;

        AddToCartRequestBuilder() {}

        public AddToCartRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public AddToCartRequestBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public AddToCartRequestBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }

        public AddToCartRequest build() {
            return new AddToCartRequest(productId, quantity, sessionId);
        }
    }
}
