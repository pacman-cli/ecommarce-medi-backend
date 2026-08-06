package com.example.ecommerce.purchase.dto.request;

import com.example.ecommerce.purchase.dto.enums.PurchasePaymentStatus;
import com.example.ecommerce.purchase.dto.enums.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Filter criteria payload for querying purchase orders.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Purchase order filtering request payload")
public class PurchaseOrderFilterRequest {

    @Schema(description = "Search query keyword matching PO number, supplier name, or invoice number", example = "PO-2026")
    private String query;

    @Schema(description = "Supplier ID filter", example = "10")
    private Long supplierId;

    @Schema(description = "Warehouse ID filter", example = "1")
    private Long warehouseId;

    @Schema(description = "Purchase order status filter", example = "ORDERED")
    private PurchaseStatus status;

    @Schema(description = "Payment status filter", example = "UNPAID")
    private PurchasePaymentStatus paymentStatus;

    @Schema(description = "Start order date filter bound", example = "2026-08-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "End order date filter bound", example = "2026-08-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
