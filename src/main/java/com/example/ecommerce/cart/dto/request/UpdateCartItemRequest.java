package com.example.ecommerce.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Payload for updating quantity of a line item in cart.
 */
@Schema(description = "Payload for updating cart item quantity")
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Updated item quantity", example = "5")
    private Integer quantity;

    public UpdateCartItemRequest() {
    }

    public UpdateCartItemRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static UpdateCartItemRequestBuilder builder() { return new UpdateCartItemRequestBuilder(); }

    public static class UpdateCartItemRequestBuilder {
        private Integer quantity;

        UpdateCartItemRequestBuilder() {}

        public UpdateCartItemRequestBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }

        public UpdateCartItemRequest build() {
            return new UpdateCartItemRequest(quantity);
        }
    }
}
