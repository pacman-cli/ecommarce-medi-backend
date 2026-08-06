package com.example.ecommerce.inventory.dto.response;

import com.example.ecommerce.inventory.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Inventory transaction history log projection DTO.
 */
@Schema(description = "Inventory transaction history record")
public class InventoryTransactionResponse {

    @Schema(description = "Transaction ID", example = "1000")
    private Long id;

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product name", example = "Paracetamol 500mg Tablets")
    private String productName;

    @Schema(description = "Product SKU", example = "MED-PARA-500")
    private String productSku;

    @Schema(description = "Stock Batch ID", example = "50")
    private Long stockBatchId;

    @Schema(description = "Batch number", example = "LOT-20260804-A")
    private String batchNumber;

    @Schema(description = "Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Warehouse name", example = "Central Distribution Hub")
    private String warehouseName;

    @Schema(description = "Supplier ID", example = "10")
    private Long supplierId;

    @Schema(description = "Supplier name", example = "Global PharmaCare Labs")
    private String supplierName;

    @Schema(description = "Transaction movement type", example = "INBOUND_PURCHASE")
    private TransactionType transactionType;

    @Schema(description = "Movement quantity (positive or negative)", example = "500")
    private Integer quantity;

    @Schema(description = "Unit price valuation", example = "3.20")
    private BigDecimal unitPrice;

    @Schema(description = "Total transaction financial value", example = "1600.00")
    private BigDecimal totalValue;

    @Schema(description = "Reference document ID or Order ID", example = "PO-2026-001")
    private String referenceNumber;

    @Schema(description = "Reason / Notes", example = "Initial purchase stock receipt")
    private String reason;

    @Schema(description = "User who authorized or performed transaction", example = "admin@example.com")
    private String performedBy;

    @Schema(description = "Transaction timestamp")
    private Instant transactionDate;

    public InventoryTransactionResponse() {
    }

    public InventoryTransactionResponse(Long id, Long productId, String productName, String productSku, Long stockBatchId, String batchNumber, Long warehouseId, String warehouseName, Long supplierId, String supplierName, TransactionType transactionType, Integer quantity, BigDecimal unitPrice, BigDecimal totalValue, String referenceNumber, String reason, String performedBy, Instant transactionDate) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.stockBatchId = stockBatchId;
        this.batchNumber = batchNumber;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalValue = totalValue;
        this.referenceNumber = referenceNumber;
        this.reason = reason;
        this.performedBy = performedBy;
        this.transactionDate = transactionDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public Long getStockBatchId() { return stockBatchId; }
    public void setStockBatchId(Long stockBatchId) { this.stockBatchId = stockBatchId; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public Instant getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Instant transactionDate) { this.transactionDate = transactionDate; }

    public static InventoryTransactionResponseBuilder builder() { return new InventoryTransactionResponseBuilder(); }

    public static class InventoryTransactionResponseBuilder {
        private Long id;
        private Long productId;
        private String productName;
        private String productSku;
        private Long stockBatchId;
        private String batchNumber;
        private Long warehouseId;
        private String warehouseName;
        private Long supplierId;
        private String supplierName;
        private TransactionType transactionType;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalValue;
        private String referenceNumber;
        private String reason;
        private String performedBy;
        private Instant transactionDate;

        InventoryTransactionResponseBuilder() {}

        public InventoryTransactionResponseBuilder id(Long id) { this.id = id; return this; }
        public InventoryTransactionResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public InventoryTransactionResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public InventoryTransactionResponseBuilder productSku(String productSku) { this.productSku = productSku; return this; }
        public InventoryTransactionResponseBuilder stockBatchId(Long stockBatchId) { this.stockBatchId = stockBatchId; return this; }
        public InventoryTransactionResponseBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public InventoryTransactionResponseBuilder warehouseId(Long warehouseId) { this.warehouseId = warehouseId; return this; }
        public InventoryTransactionResponseBuilder warehouseName(String warehouseName) { this.warehouseName = warehouseName; return this; }
        public InventoryTransactionResponseBuilder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
        public InventoryTransactionResponseBuilder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
        public InventoryTransactionResponseBuilder transactionType(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public InventoryTransactionResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public InventoryTransactionResponseBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public InventoryTransactionResponseBuilder totalValue(BigDecimal totalValue) { this.totalValue = totalValue; return this; }
        public InventoryTransactionResponseBuilder referenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }
        public InventoryTransactionResponseBuilder reason(String reason) { this.reason = reason; return this; }
        public InventoryTransactionResponseBuilder performedBy(String performedBy) { this.performedBy = performedBy; return this; }
        public InventoryTransactionResponseBuilder transactionDate(Instant transactionDate) { this.transactionDate = transactionDate; return this; }

        public InventoryTransactionResponse build() {
            return new InventoryTransactionResponse(id, productId, productName, productSku, stockBatchId, batchNumber, warehouseId, warehouseName, supplierId, supplierName, transactionType, quantity, unitPrice, totalValue, referenceNumber, reason, performedBy, transactionDate);
        }
    }
}
