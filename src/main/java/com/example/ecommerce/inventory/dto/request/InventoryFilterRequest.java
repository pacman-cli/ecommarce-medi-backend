package com.example.ecommerce.inventory.dto.request;

import com.example.ecommerce.inventory.entity.BatchStatus;
import com.example.ecommerce.inventory.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Filter options for querying stock batches, transaction history, and inventory alerts.
 */
@Schema(description = "Inventory filter criteria")
public class InventoryFilterRequest {

    @Schema(description = "Search term matching batch number, barcode, SKU, product or warehouse name", example = "LOT-2026")
    private String search;

    @Schema(description = "Filter by Product ID", example = "200")
    private Long productId;

    @Schema(description = "Filter by Warehouse ID", example = "1")
    private Long warehouseId;

    @Schema(description = "Filter by Supplier ID", example = "10")
    private Long supplierId;

    @Schema(description = "Filter by Batch Status", example = "AVAILABLE")
    private BatchStatus status;

    @Schema(description = "Filter by Transaction Type", example = "INBOUND_PURCHASE")
    private TransactionType transactionType;

    @Schema(description = "Exact barcode filter", example = "8901122334455")
    private String barcode;

    @Schema(description = "Exact QR Code string filter", example = "https://example.com/qr/batch/LOT-20260804-A")
    private String qrCode;

    @Schema(description = "Filter low stock items", example = "true")
    private Boolean lowStockOnly;

    @Schema(description = "Filter expired batches", example = "true")
    private Boolean expiredOnly;

    public InventoryFilterRequest() {
    }

    public InventoryFilterRequest(String search, Long productId, Long warehouseId, Long supplierId, BatchStatus status, TransactionType transactionType, String barcode, String qrCode, Boolean lowStockOnly, Boolean expiredOnly) {
        this.search = search;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.supplierId = supplierId;
        this.status = status;
        this.transactionType = transactionType;
        this.barcode = barcode;
        this.qrCode = qrCode;
        this.lowStockOnly = lowStockOnly;
        this.expiredOnly = expiredOnly;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public BatchStatus getStatus() { return status; }
    public void setStatus(BatchStatus status) { this.status = status; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public Boolean getLowStockOnly() { return lowStockOnly; }
    public void setLowStockOnly(Boolean lowStockOnly) { this.lowStockOnly = lowStockOnly; }

    public Boolean getExpiredOnly() { return expiredOnly; }
    public void setExpiredOnly(Boolean expiredOnly) { this.expiredOnly = expiredOnly; }

    public static InventoryFilterRequestBuilder builder() { return new InventoryFilterRequestBuilder(); }

    public static class InventoryFilterRequestBuilder {
        private String search;
        private Long productId;
        private Long warehouseId;
        private Long supplierId;
        private BatchStatus status;
        private TransactionType transactionType;
        private String barcode;
        private String qrCode;
        private Boolean lowStockOnly;
        private Boolean expiredOnly;

        InventoryFilterRequestBuilder() {}

        public InventoryFilterRequestBuilder search(String search) { this.search = search; return this; }
        public InventoryFilterRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public InventoryFilterRequestBuilder warehouseId(Long warehouseId) { this.warehouseId = warehouseId; return this; }
        public InventoryFilterRequestBuilder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
        public InventoryFilterRequestBuilder status(BatchStatus status) { this.status = status; return this; }
        public InventoryFilterRequestBuilder transactionType(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public InventoryFilterRequestBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public InventoryFilterRequestBuilder qrCode(String qrCode) { this.qrCode = qrCode; return this; }
        public InventoryFilterRequestBuilder lowStockOnly(Boolean lowStockOnly) { this.lowStockOnly = lowStockOnly; return this; }
        public InventoryFilterRequestBuilder expiredOnly(Boolean expiredOnly) { this.expiredOnly = expiredOnly; return this; }

        public InventoryFilterRequest build() {
            return new InventoryFilterRequest(search, productId, warehouseId, supplierId, status, transactionType, barcode, qrCode, lowStockOnly, expiredOnly);
        }
    }
}
