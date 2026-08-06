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
import java.util.List;

/**
 * Detailed purchase order response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Purchase order details payload")
public class PurchaseOrderResponse {

    @Schema(description = "Purchase Order ID", example = "1001")
    private Long id;

    @Schema(description = "Unique PO number identifier", example = "PO-20260805-001001")
    private String poNumber;

    @Schema(description = "Supplier vendor ID", example = "10")
    private Long supplierId;

    @Schema(description = "Supplier company name", example = "Square Pharmaceuticals PLC")
    private String supplierName;

    @Schema(description = "Supplier code", example = "SUP-PHARMA-01")
    private String supplierCode;

    @Schema(description = "Destination warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Destination warehouse name", example = "Central Distribution Hub")
    private String warehouseName;

    @Schema(description = "Purchase status", example = "RECEIVED")
    private PurchaseStatus status;

    @Schema(description = "Payment status", example = "PAID")
    private PurchasePaymentStatus paymentStatus;

    @Schema(description = "Order date", example = "2026-08-05")
    private LocalDate orderDate;

    @Schema(description = "Expected delivery date", example = "2026-08-15")
    private LocalDate expectedDeliveryDate;

    @Schema(description = "Supplier invoice number", example = "INV-2026-9901")
    private String invoiceNumber;

    @Schema(description = "Supplier invoice date", example = "2026-08-05")
    private LocalDate invoiceDate;

    @Schema(description = "Subtotal amount", example = "1050.00")
    private BigDecimal subtotal;

    @Schema(description = "Tax amount", example = "50.00")
    private BigDecimal taxAmount;

    @Schema(description = "Shipping freight cost", example = "120.00")
    private BigDecimal shippingCost;

    @Schema(description = "Total purchase order amount", example = "1220.00")
    private BigDecimal totalAmount;

    @Schema(description = "Paid amount", example = "1220.00")
    private BigDecimal paidAmount;

    @Schema(description = "Remaining unpaid balance amount", example = "0.00")
    private BigDecimal remainingBalance;

    @Schema(description = "Special notes", example = "Urgent replenishment for seasonal demand")
    private String notes;

    @Schema(description = "Line items list")
    private List<PurchaseItemResponse> items;

    @Schema(description = "Total ordered items count", example = "100")
    private Integer totalOrderedQuantity;

    @Schema(description = "Total received items count", example = "100")
    private Integer totalReceivedQuantity;

    @Schema(description = "PO creation timestamp", example = "2026-08-05T14:00:00Z")
    private Instant createdAt;

    @Schema(description = "PO update timestamp", example = "2026-08-05T14:00:00Z")
    private Instant updatedAt;
}
