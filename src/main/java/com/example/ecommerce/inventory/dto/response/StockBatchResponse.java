package com.example.ecommerce.inventory.dto.response;

import com.example.ecommerce.inventory.entity.BatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Stock batch projection response DTO.
 */
@Schema(description = "Stock batch response")
public class StockBatchResponse {

    @Schema(description = "Batch ID", example = "50")
    private Long id;

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product name", example = "Paracetamol 500mg Tablets")
    private String productName;

    @Schema(description = "Product SKU", example = "MED-PARA-500")
    private String productSku;

    @Schema(description = "Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Warehouse name", example = "Central Distribution Hub")
    private String warehouseName;

    @Schema(description = "Supplier ID", example = "10")
    private Long supplierId;

    @Schema(description = "Supplier name", example = "Global PharmaCare Labs")
    private String supplierName;

    @Schema(description = "Batch number", example = "LOT-20260804-A")
    private String batchNumber;

    @Schema(description = "Manufacturing date", example = "2026-01-15")
    private LocalDate manufacturingDate;

    @Schema(description = "Expiry date", example = "2028-01-15")
    private LocalDate expiryDate;

    @Schema(description = "Purchase price", example = "3.20")
    private BigDecimal purchasePrice;

    @Schema(description = "Selling price", example = "5.99")
    private BigDecimal sellingPrice;

    @Schema(description = "Total quantity", example = "500")
    private Integer quantity;

    @Schema(description = "Available quantity", example = "480")
    private Integer availableQuantity;

    @Schema(description = "Reserved quantity", example = "20")
    private Integer reservedQuantity;

    @Schema(description = "Barcode", example = "8901122334455")
    private String barcode;

    @Schema(description = "QR Code string", example = "https://example.com/qr/batch/LOT-20260804-A")
    private String qrCode;

    @Schema(description = "Batch status", example = "AVAILABLE")
    private BatchStatus status;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    private Instant updatedAt;

    public StockBatchResponse() {
    }

    public StockBatchResponse(Long id, Long productId, String productName, String productSku, Long warehouseId, String warehouseName, Long supplierId, String supplierName, String batchNumber, LocalDate manufacturingDate, LocalDate expiryDate, BigDecimal purchasePrice, BigDecimal sellingPrice, Integer quantity, Integer availableQuantity, Integer reservedQuantity, String barcode, String qrCode, BatchStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.batchNumber = batchNumber;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.barcode = barcode;
        this.qrCode = qrCode;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public LocalDate getManufacturingDate() { return manufacturingDate; }
    public void setManufacturingDate(LocalDate manufacturingDate) { this.manufacturingDate = manufacturingDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public BatchStatus getStatus() { return status; }
    public void setStatus(BatchStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static StockBatchResponseBuilder builder() { return new StockBatchResponseBuilder(); }

    public static class StockBatchResponseBuilder {
        private Long id;
        private Long productId;
        private String productName;
        private String productSku;
        private Long warehouseId;
        private String warehouseName;
        private Long supplierId;
        private String supplierName;
        private String batchNumber;
        private LocalDate manufacturingDate;
        private LocalDate expiryDate;
        private BigDecimal purchasePrice;
        private BigDecimal sellingPrice;
        private Integer quantity;
        private Integer availableQuantity;
        private Integer reservedQuantity;
        private String barcode;
        private String qrCode;
        private BatchStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        StockBatchResponseBuilder() {}

        public StockBatchResponseBuilder id(Long id) { this.id = id; return this; }
        public StockBatchResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public StockBatchResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public StockBatchResponseBuilder productSku(String productSku) { this.productSku = productSku; return this; }
        public StockBatchResponseBuilder warehouseId(Long warehouseId) { this.warehouseId = warehouseId; return this; }
        public StockBatchResponseBuilder warehouseName(String warehouseName) { this.warehouseName = warehouseName; return this; }
        public StockBatchResponseBuilder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
        public StockBatchResponseBuilder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
        public StockBatchResponseBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public StockBatchResponseBuilder manufacturingDate(LocalDate manufacturingDate) { this.manufacturingDate = manufacturingDate; return this; }
        public StockBatchResponseBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public StockBatchResponseBuilder purchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; return this; }
        public StockBatchResponseBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public StockBatchResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public StockBatchResponseBuilder availableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; return this; }
        public StockBatchResponseBuilder reservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public StockBatchResponseBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public StockBatchResponseBuilder qrCode(String qrCode) { this.qrCode = qrCode; return this; }
        public StockBatchResponseBuilder status(BatchStatus status) { this.status = status; return this; }
        public StockBatchResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public StockBatchResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public StockBatchResponse build() {
            return new StockBatchResponse(id, productId, productName, productSku, warehouseId, warehouseName, supplierId, supplierName, batchNumber, manufacturingDate, expiryDate, purchasePrice, sellingPrice, quantity, availableQuantity, reservedQuantity, barcode, qrCode, status, createdAt, updatedAt);
        }
    }
}
