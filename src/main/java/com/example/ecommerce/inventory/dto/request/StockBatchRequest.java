package com.example.ecommerce.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payload for receiving or creating a new stock batch.
 */
@Schema(description = "Payload for receiving or creating stock batch")
public class StockBatchRequest {

    @NotNull(message = "Product ID is required")
    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @NotNull(message = "Warehouse ID is required")
    @Schema(description = "Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Supplier ID", example = "10")
    private Long supplierId;

    @NotBlank(message = "Batch number is required")
    @Size(max = 100, message = "Batch number must not exceed 100 characters")
    @Schema(description = "Lot / Batch number", example = "LOT-20260804-A")
    private String batchNumber;

    @Schema(description = "Manufacturing date", example = "2026-01-15")
    private LocalDate manufacturingDate;

    @Schema(description = "Expiration date", example = "2028-01-15")
    private LocalDate expiryDate;

    @DecimalMin(value = "0.0", message = "Purchase price must be non-negative")
    @Schema(description = "Purchase unit price", example = "3.20")
    private BigDecimal purchasePrice;

    @DecimalMin(value = "0.0", message = "Selling price must be non-negative")
    @Schema(description = "Selling unit price", example = "5.99")
    private BigDecimal sellingPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    @Schema(description = "Received quantity", example = "500")
    private Integer quantity;

    @Size(max = 100, message = "Barcode must not exceed 100 characters")
    @Schema(description = "Batch barcode", example = "8901122334455")
    private String barcode;

    @Size(max = 300, message = "QR code string must not exceed 300 characters")
    @Schema(description = "Batch QR code content string/URL", example = "https://example.com/qr/batch/LOT-20260804-A")
    private String qrCode;

    public StockBatchRequest() {
    }

    public StockBatchRequest(Long productId, Long warehouseId, Long supplierId, String batchNumber, LocalDate manufacturingDate, LocalDate expiryDate, BigDecimal purchasePrice, BigDecimal sellingPrice, Integer quantity, String barcode, String qrCode) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.supplierId = supplierId;
        this.batchNumber = batchNumber;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.barcode = barcode;
        this.qrCode = qrCode;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

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

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public static StockBatchRequestBuilder builder() { return new StockBatchRequestBuilder(); }

    public static class StockBatchRequestBuilder {
        private Long productId;
        private Long warehouseId;
        private Long supplierId;
        private String batchNumber;
        private LocalDate manufacturingDate;
        private LocalDate expiryDate;
        private BigDecimal purchasePrice;
        private BigDecimal sellingPrice;
        private Integer quantity;
        private String barcode;
        private String qrCode;

        StockBatchRequestBuilder() {}

        public StockBatchRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public StockBatchRequestBuilder warehouseId(Long warehouseId) { this.warehouseId = warehouseId; return this; }
        public StockBatchRequestBuilder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
        public StockBatchRequestBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public StockBatchRequestBuilder manufacturingDate(LocalDate manufacturingDate) { this.manufacturingDate = manufacturingDate; return this; }
        public StockBatchRequestBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public StockBatchRequestBuilder purchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; return this; }
        public StockBatchRequestBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public StockBatchRequestBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public StockBatchRequestBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public StockBatchRequestBuilder qrCode(String qrCode) { this.qrCode = qrCode; return this; }

        public StockBatchRequest build() {
            return new StockBatchRequest(productId, warehouseId, supplierId, batchNumber, manufacturingDate, expiryDate, purchasePrice, sellingPrice, quantity, barcode, qrCode);
        }
    }
}
