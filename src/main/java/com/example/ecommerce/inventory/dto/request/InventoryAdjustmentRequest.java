package com.example.ecommerce.inventory.dto.request;

import com.example.ecommerce.inventory.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Payload for performing stock adjustments, returns, write-offs, or damaged goods handling.
 */
@Schema(description = "Payload for manual inventory adjustment")
public class InventoryAdjustmentRequest {

    @Schema(description = "Associated stock batch ID (if adjusting specific batch)", example = "50")
    private Long stockBatchId;

    @NotNull(message = "Product ID is required")
    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Supplier ID", example = "10")
    private Long supplierId;

    @NotNull(message = "Transaction type is required")
    @Schema(description = "Transaction movement type", example = "ADJUSTMENT_INCREASE")
    private TransactionType transactionType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Adjustment quantity must be at least 1")
    @Schema(description = "Adjustment unit quantity", example = "10")
    private Integer quantity;

    @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
    @Schema(description = "Unit price valuation", example = "5.99")
    private BigDecimal unitPrice;

    @NotBlank(message = "Adjustment reason is required")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    @Schema(description = "Justification / Audit reason for stock movement", example = "Quarterly physical count audit correction")
    private String reason;

    @Size(max = 100, message = "Reference number must not exceed 100 characters")
    @Schema(description = "Reference document ID or Audit PO", example = "AUDIT-2026-Q3")
    private String referenceNumber;

    public InventoryAdjustmentRequest() {
    }

    public InventoryAdjustmentRequest(Long stockBatchId, Long productId, Long warehouseId, Long supplierId, TransactionType transactionType, Integer quantity, BigDecimal unitPrice, String reason, String referenceNumber) {
        this.stockBatchId = stockBatchId;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.supplierId = supplierId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.reason = reason;
        this.referenceNumber = referenceNumber;
    }

    public Long getStockBatchId() { return stockBatchId; }
    public void setStockBatchId(Long stockBatchId) { this.stockBatchId = stockBatchId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public static InventoryAdjustmentRequestBuilder builder() { return new InventoryAdjustmentRequestBuilder(); }

    public static class InventoryAdjustmentRequestBuilder {
        private Long stockBatchId;
        private Long productId;
        private Long warehouseId;
        private Long supplierId;
        private TransactionType transactionType;
        private Integer quantity;
        private BigDecimal unitPrice;
        private String reason;
        private String referenceNumber;

        InventoryAdjustmentRequestBuilder() {}

        public InventoryAdjustmentRequestBuilder stockBatchId(Long stockBatchId) { this.stockBatchId = stockBatchId; return this; }
        public InventoryAdjustmentRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public InventoryAdjustmentRequestBuilder warehouseId(Long warehouseId) { this.warehouseId = warehouseId; return this; }
        public InventoryAdjustmentRequestBuilder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
        public InventoryAdjustmentRequestBuilder transactionType(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public InventoryAdjustmentRequestBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public InventoryAdjustmentRequestBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public InventoryAdjustmentRequestBuilder reason(String reason) { this.reason = reason; return this; }
        public InventoryAdjustmentRequestBuilder referenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }

        public InventoryAdjustmentRequest build() {
            return new InventoryAdjustmentRequest(stockBatchId, productId, warehouseId, supplierId, transactionType, quantity, unitPrice, reason, referenceNumber);
        }
    }
}
