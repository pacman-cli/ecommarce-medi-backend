package com.example.ecommerce.purchase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payload for initializing a new purchase order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Purchase order creation request payload")
public class CreatePurchaseOrderRequest {

    @Schema(description = "Supplier vendor ID", example = "10")
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @Schema(description = "Destination warehouse ID", example = "1")
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @Schema(description = "Order date", example = "2026-08-05")
    @NotNull(message = "Order date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate orderDate;

    @Schema(description = "Expected delivery date", example = "2026-08-15")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedDeliveryDate;

    @Schema(description = "Supplier invoice number", example = "INV-2026-9901")
    private String invoiceNumber;

    @Schema(description = "Supplier invoice date", example = "2026-08-05")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDate;

    @Schema(description = "Tax amount", example = "50.00")
    @DecimalMin(value = "0.0", message = "Tax amount cannot be negative")
    private BigDecimal taxAmount;

    @Schema(description = "Shipping freight cost", example = "120.00")
    @DecimalMin(value = "0.0", message = "Shipping cost cannot be negative")
    private BigDecimal shippingCost;

    @Schema(description = "Notes or special instructions", example = "Urgent replenishment for seasonal demand")
    private String notes;

    @Schema(description = "Purchase order line items")
    @NotEmpty(message = "Purchase items list cannot be empty")
    @Valid
    private List<PurchaseItemRequest> items;
}
