package com.example.ecommerce.dashboard.dto.response;

import com.example.ecommerce.product.entity.StockStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Summary DTO representing a product at or below low stock threshold.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Low stock product warning entry")
public class LowStockProductResponse {

    @Schema(description = "Product ID", example = "101")
    private Long id;

    @Schema(description = "Product Name", example = "Paracetamol Extra 500mg")
    private String name;

    @Schema(description = "Stock Keeping Unit (SKU)", example = "MED-PAR-500")
    private String sku;

    @Schema(description = "Category Name", example = "Analgesics")
    private String categoryName;

    @Schema(description = "Current available quantity in inventory", example = "3")
    private Integer currentStock;

    @Schema(description = "Configured low stock alert threshold", example = "10")
    private Integer minStockThreshold;

    @Schema(description = "Selling unit price", example = "15.99")
    private BigDecimal price;

    @Schema(description = "Current stock operational status", example = "LOW_STOCK")
    private StockStatus stockStatus;

    @Schema(description = "Thumbnail image URL", example = "https://cdn.example.com/images/med1.png")
    private String thumbnail;
}
