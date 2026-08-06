package com.example.ecommerce.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Payload for updating product stock levels and inventory thresholds.
 */
@Schema(description = "Payload for stock quantity update")
public class UpdateStockRequest {

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    @Schema(description = "New total stock quantity", example = "100")
    private Integer quantity;

    @Min(value = 0, message = "Reserved quantity must be non-negative")
    @Schema(description = "Updated reserved quantity", example = "2")
    private Integer reservedQuantity;

    @Min(value = 0, message = "Low stock threshold must be non-negative")
    @Schema(description = "Updated low stock threshold", example = "10")
    private Integer lowStock;

    public UpdateStockRequest() {
    }

    public UpdateStockRequest(Integer quantity, Integer reservedQuantity, Integer lowStock) {
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.lowStock = lowStock;
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public Integer getLowStock() { return lowStock; }
    public void setLowStock(Integer lowStock) { this.lowStock = lowStock; }

    public static UpdateStockRequestBuilder builder() { return new UpdateStockRequestBuilder(); }

    public static class UpdateStockRequestBuilder {
        private Integer quantity;
        private Integer reservedQuantity;
        private Integer lowStock;

        UpdateStockRequestBuilder() {}

        public UpdateStockRequestBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public UpdateStockRequestBuilder reservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public UpdateStockRequestBuilder lowStock(Integer lowStock) { this.lowStock = lowStock; return this; }

        public UpdateStockRequest build() {
            return new UpdateStockRequest(quantity, reservedQuantity, lowStock);
        }
    }
}
