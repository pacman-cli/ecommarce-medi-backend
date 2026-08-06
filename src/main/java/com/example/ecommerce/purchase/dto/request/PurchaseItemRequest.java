package com.example.ecommerce.purchase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payload for specifying line items in a purchase order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Purchase order line item request payload")
public class PurchaseItemRequest {

    @Schema(description = "Product ID", example = "200")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @Schema(description = "Ordered quantity", example = "100")
    @NotNull(message = "Ordered quantity is required")
    @Min(value = 1, message = "Ordered quantity must be at least 1")
    private Integer orderedQuantity;

    @Schema(description = "Unit purchase cost", example = "10.50")
    @NotNull(message = "Unit cost is required")
    @DecimalMin(value = "0.0", message = "Unit cost cannot be negative")
    private BigDecimal unitCost;

    @Schema(description = "Item notes", example = "Batch lot requirement #2026")
    private String notes;
}
