package com.example.ecommerce.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Summary DTO reporting low stock, out of stock or expired inventory batches.
 */
@Schema(description = "Inventory stock alert summary")
public class InventoryAlertResponse {

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product name", example = "Paracetamol 500mg Tablets")
    private String productName;

    @Schema(description = "Product SKU", example = "MED-PARA-500")
    private String productSku;

    @Schema(description = "Aggregated stock quantity across all active batches", example = "3")
    private Integer totalStock;

    @Schema(description = "Alert type indicator", example = "LOW_STOCK")
    private String alertType;

    @Schema(description = "Associated stock batches triggering or related to alert")
    private List<StockBatchResponse> batches;

    public InventoryAlertResponse() {
    }

    public InventoryAlertResponse(Long productId, String productName, String productSku, Integer totalStock, String alertType, List<StockBatchResponse> batches) {
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.totalStock = totalStock;
        this.alertType = alertType;
        this.batches = batches;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public Integer getTotalStock() { return totalStock; }
    public void setTotalStock(Integer totalStock) { this.totalStock = totalStock; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public List<StockBatchResponse> getBatches() { return batches; }
    public void setBatches(List<StockBatchResponse> batches) { this.batches = batches; }

    public static InventoryAlertResponseBuilder builder() { return new InventoryAlertResponseBuilder(); }

    public static class InventoryAlertResponseBuilder {
        private Long productId;
        private String productName;
        private String productSku;
        private Integer totalStock;
        private String alertType;
        private List<StockBatchResponse> batches;

        InventoryAlertResponseBuilder() {}

        public InventoryAlertResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public InventoryAlertResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public InventoryAlertResponseBuilder productSku(String productSku) { this.productSku = productSku; return this; }
        public InventoryAlertResponseBuilder totalStock(Integer totalStock) { this.totalStock = totalStock; return this; }
        public InventoryAlertResponseBuilder alertType(String alertType) { this.alertType = alertType; return this; }
        public InventoryAlertResponseBuilder batches(List<StockBatchResponse> batches) { this.batches = batches; return this; }

        public InventoryAlertResponse build() {
            return new InventoryAlertResponse(productId, productName, productSku, totalStock, alertType, batches);
        }
    }
}
