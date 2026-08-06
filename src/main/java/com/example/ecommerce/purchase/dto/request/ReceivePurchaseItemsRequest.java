package com.example.ecommerce.purchase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Payload for receiving goods for purchase order items and creating stock batches.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Purchase receiving request payload")
public class ReceivePurchaseItemsRequest {

    @Schema(description = "List of item receiving quantities")
    @NotNull(message = "Item receiving list cannot be null")
    private List<ItemReceivingEntry> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Individual item receiving quantity entry")
    public static class ItemReceivingEntry {

        @Schema(description = "Purchase Item ID", example = "501")
        @NotNull(message = "Purchase item ID is required")
        private Long itemId;

        @Schema(description = "Quantity received in this batch shipment", example = "50")
        @NotNull(message = "Quantity received is required")
        @Min(value = 1, message = "Received quantity must be at least 1")
        private Integer quantityReceived;

        @Schema(description = "Batch number for inventory stock batch creation", example = "LOT-20260805-A")
        private String batchNumber;

        @Schema(description = "Batch expiration date", example = "2027-12-31")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate expiryDate;
    }
}
