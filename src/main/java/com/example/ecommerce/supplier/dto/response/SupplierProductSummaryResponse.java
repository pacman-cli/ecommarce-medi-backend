package com.example.ecommerce.supplier.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO summarizing a product supplied by a vendor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product supplied by vendor summary")
public class SupplierProductSummaryResponse {

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product Name", example = "Napa 500mg Tablet")
    private String productName;

    @Schema(description = "Product SKU", example = "MED-NAPA-500")
    private String sku;

    @Schema(description = "Brand Name", example = "Square")
    private String brandName;

    @Schema(description = "Category Name", example = "Pain Relief")
    private String categoryName;

    @Schema(description = "Retail selling price", example = "15.00")
    private BigDecimal price;

    @Schema(description = "Current available stock quantity across batches", example = "450")
    private Integer totalQuantityInStock;

    @Schema(description = "Count of active stock batches from this supplier", example = "3")
    private Integer totalBatchesCount;
}
