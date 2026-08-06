package com.example.ecommerce.supplier.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO representing a stock batch purchase receiving history log entry.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Supplier purchase stock batch receiving history log entry")
public class SupplierPurchaseHistoryResponse {

    @Schema(description = "Stock Batch ID", example = "50")
    private Long batchId;

    @Schema(description = "Lot number identifier", example = "LOT-20260804-A")
    private String lotNumber;

    @Schema(description = "Associated Product ID", example = "200")
    private Long productId;

    @Schema(description = "Associated Product Name", example = "Napa 500mg Tablet")
    private String productName;

    @Schema(description = "Associated Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Associated Warehouse Name", example = "Central Distribution Hub")
    private String warehouseName;

    @Schema(description = "Quantity received", example = "500")
    private Integer initialQuantity;

    @Schema(description = "Current remaining quantity", example = "450")
    private Integer currentQuantity;

    @Schema(description = "Purchase unit price cost", example = "10.00")
    private BigDecimal purchasePrice;

    @Schema(description = "Total batch purchase cost", example = "5000.00")
    private BigDecimal totalBatchCost;

    @Schema(description = "Batch expiration date", example = "2027-12-31")
    private LocalDate expirationDate;

    @Schema(description = "Receiving timestamp", example = "2026-08-04T10:00:00Z")
    private Instant receivedAt;
}
