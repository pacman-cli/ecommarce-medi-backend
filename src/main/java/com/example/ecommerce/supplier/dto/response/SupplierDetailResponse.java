package com.example.ecommerce.supplier.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Detailed supplier profile including summary aggregate statistics and purchase histories.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detailed supplier profile with purchase metrics payload")
public class SupplierDetailResponse {

    @Schema(description = "Supplier profile basic information")
    private SupplierResponse profile;

    @Schema(description = "Total distinct products supplied count", example = "12")
    private Integer totalProductsSupplied;

    @Schema(description = "Total stock batches received count", example = "45")
    private Integer totalBatchesReceived;

    @Schema(description = "Cumulative purchase expenditure amount", example = "125000.00")
    private BigDecimal totalPurchaseExpenditure;

    @Schema(description = "List of products supplied by vendor")
    private List<SupplierProductSummaryResponse> products;

    @Schema(description = "Recent purchase batch receiving history")
    private List<SupplierPurchaseHistoryResponse> purchaseHistory;
}
