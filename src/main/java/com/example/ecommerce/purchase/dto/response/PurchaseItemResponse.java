package com.example.ecommerce.purchase.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Line item detail response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Purchase order line item detail payload")
public class PurchaseItemResponse {

    @Schema(description = "Purchase Item ID", example = "501")
    private Long id;

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product Name", example = "Napa 500mg Tablet")
    private String productName;

    @Schema(description = "Product SKU", example = "MED-NAPA-500")
    private String productSku;

    @Schema(description = "Ordered quantity", example = "100")
    private Integer orderedQuantity;

    @Schema(description = "Received quantity", example = "100")
    private Integer receivedQuantity;

    @Schema(description = "Unit purchase cost", example = "10.50")
    private BigDecimal unitCost;

    @Schema(description = "Total line item cost", example = "1050.00")
    private BigDecimal totalCost;

    @Schema(description = "Item notes", example = "Batch lot requirement #2026")
    private String notes;
}
