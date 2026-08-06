package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Health summary DTO for store catalogue inventory valuation and stock levels.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Inventory stock evaluation and health summary")
public class InventorySummaryResponse {

    @Schema(description = "Total distinct active products in catalogue", example = "350")
    private Long totalProducts;

    @Schema(description = "Count of products with healthy stock levels", example = "310")
    private Long inStockProducts;

    @Schema(description = "Count of products at or below low-stock threshold", example = "25")
    private Long lowStockProducts;

    @Schema(description = "Count of completely out-of-stock products", example = "15")
    private Long outOfStockProducts;

    @Schema(description = "Total physical units count across all stock", example = "18450")
    private Long totalQuantityInStock;

    @Schema(description = "Total monetary valuation of current stock inventory", example = "245800.00")
    private BigDecimal totalInventoryValue;
}
