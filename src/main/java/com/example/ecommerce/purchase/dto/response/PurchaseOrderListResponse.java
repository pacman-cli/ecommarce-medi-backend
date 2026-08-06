package com.example.ecommerce.purchase.dto.response;

import com.example.ecommerce.purchase.dto.enums.PurchasePaymentStatus;
import com.example.ecommerce.purchase.dto.enums.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Lightweight purchase order list response item DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Lightweight purchase order list summary payload")
public class PurchaseOrderListResponse {

    @Schema(description = "Purchase Order ID", example = "1001")
    private Long id;

    @Schema(description = "Unique PO number", example = "PO-20260805-001001")
    private String poNumber;

    @Schema(description = "Supplier vendor ID", example = "10")
    private Long supplierId;

    @Schema(description = "Supplier company name", example = "Square Pharmaceuticals PLC")
    private String supplierName;

    @Schema(description = "Destination warehouse name", example = "Central Distribution Hub")
    private String warehouseName;

    @Schema(description = "Purchase status", example = "ORDERED")
    private PurchaseStatus status;

    @Schema(description = "Payment status", example = "UNPAID")
    private PurchasePaymentStatus paymentStatus;

    @Schema(description = "Order date", example = "2026-08-05")
    private LocalDate orderDate;

    @Schema(description = "Invoice number", example = "INV-2026-9901")
    private String invoiceNumber;

    @Schema(description = "Total purchase order amount", example = "1220.00")
    private BigDecimal totalAmount;

    @Schema(description = "Paid amount", example = "0.00")
    private BigDecimal paidAmount;

    @Schema(description = "Total items count", example = "5")
    private Integer totalItemsCount;

    @Schema(description = "PO creation timestamp", example = "2026-08-05T14:00:00Z")
    private Instant createdAt;
}
