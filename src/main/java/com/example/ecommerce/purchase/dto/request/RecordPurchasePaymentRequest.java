package com.example.ecommerce.purchase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payload for recording payment towards a purchase order invoice.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Purchase payment recording request payload")
public class RecordPurchasePaymentRequest {

    @Schema(description = "Payment amount", example = "5000.00")
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    private BigDecimal amount;

    @Schema(description = "Payment method description or reference", example = "Bank Transfer - TXN-998822")
    private String paymentReference;

    @Schema(description = "Payment note or description", example = "Partial payment for PO invoice #INV-2026-9901")
    private String notes;
}
