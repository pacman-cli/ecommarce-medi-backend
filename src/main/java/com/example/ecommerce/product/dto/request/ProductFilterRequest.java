package com.example.ecommerce.product.dto.request;

import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Filter options for dynamic searching and paginated retrieval of products.
 */
@Schema(description = "Product filter criteria")
public class ProductFilterRequest {

    @Schema(description = "Search term matching name, SKU, barcode, generic name, manufacturer or keywords", example = "Paracetamol")
    private String search;

    @Schema(description = "Exact or partial name filter", example = "Paracetamol")
    private String name;

    @Schema(description = "Exact SKU filter", example = "MED-PARA-500")
    private String sku;

    @Schema(description = "Exact barcode filter", example = "8901234567890")
    private String barcode;

    @Schema(description = "Filter by Brand ID", example = "5")
    private Long brandId;

    @Schema(description = "Filter by Category ID", example = "12")
    private Long categoryId;

    @Schema(description = "Minimum selling price", example = "10.00")
    private BigDecimal minPrice;

    @Schema(description = "Maximum selling price", example = "100.00")
    private BigDecimal maxPrice;

    @Schema(description = "Filter by status", example = "ACTIVE")
    private ProductStatus status;

    @Schema(description = "Filter by stock status", example = "IN_STOCK")
    private StockStatus stockStatus;

    @Schema(description = "Filter by prescription requirement", example = "true")
    private Boolean prescriptionRequired;

    @Schema(description = "Filter featured products", example = "true")
    private Boolean featured;

    @Schema(description = "Filter bestseller products", example = "true")
    private Boolean bestseller;

    @Schema(description = "Filter new arrivals", example = "true")
    private Boolean newArrival;

    @Schema(description = "Filter trending products", example = "true")
    private Boolean trending;

    @Schema(description = "Filter recommended products", example = "true")
    private Boolean recommended;

    @Schema(description = "Include inactive products", example = "false")
    private Boolean includeInactive;

    public ProductFilterRequest() {
    }

    public ProductFilterRequest(String search, String name, String sku, String barcode, Long brandId, Long categoryId,
                                BigDecimal minPrice, BigDecimal maxPrice, ProductStatus status, StockStatus stockStatus,
                                Boolean prescriptionRequired, Boolean featured, Boolean bestseller, Boolean newArrival,
                                Boolean trending, Boolean recommended, Boolean includeInactive) {
        this.search = search;
        this.name = name;
        this.sku = sku;
        this.barcode = barcode;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.status = status;
        this.stockStatus = stockStatus;
        this.prescriptionRequired = prescriptionRequired;
        this.featured = featured;
        this.bestseller = bestseller;
        this.newArrival = newArrival;
        this.trending = trending;
        this.recommended = recommended;
        this.includeInactive = includeInactive;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

    public StockStatus getStockStatus() { return stockStatus; }
    public void setStockStatus(StockStatus stockStatus) { this.stockStatus = stockStatus; }

    public Boolean getPrescriptionRequired() { return prescriptionRequired; }
    public void setPrescriptionRequired(Boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; }

    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }

    public Boolean getBestseller() { return bestseller; }
    public void setBestseller(Boolean bestseller) { this.bestseller = bestseller; }

    public Boolean getNewArrival() { return newArrival; }
    public void setNewArrival(Boolean newArrival) { this.newArrival = newArrival; }

    public Boolean getTrending() { return trending; }
    public void setTrending(Boolean trending) { this.trending = trending; }

    public Boolean getRecommended() { return recommended; }
    public void setRecommended(Boolean recommended) { this.recommended = recommended; }

    public Boolean getIncludeInactive() { return includeInactive; }
    public void setIncludeInactive(Boolean includeInactive) { this.includeInactive = includeInactive; }

    public static ProductFilterRequestBuilder builder() { return new ProductFilterRequestBuilder(); }

    public static class ProductFilterRequestBuilder {
        private String search;
        private String name;
        private String sku;
        private String barcode;
        private Long brandId;
        private Long categoryId;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private ProductStatus status;
        private StockStatus stockStatus;
        private Boolean prescriptionRequired;
        private Boolean featured;
        private Boolean bestseller;
        private Boolean newArrival;
        private Boolean trending;
        private Boolean recommended;
        private Boolean includeInactive;

        ProductFilterRequestBuilder() {}

        public ProductFilterRequestBuilder search(String search) { this.search = search; return this; }
        public ProductFilterRequestBuilder name(String name) { this.name = name; return this; }
        public ProductFilterRequestBuilder sku(String sku) { this.sku = sku; return this; }
        public ProductFilterRequestBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public ProductFilterRequestBuilder brandId(Long brandId) { this.brandId = brandId; return this; }
        public ProductFilterRequestBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public ProductFilterRequestBuilder minPrice(BigDecimal minPrice) { this.minPrice = minPrice; return this; }
        public ProductFilterRequestBuilder maxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; return this; }
        public ProductFilterRequestBuilder status(ProductStatus status) { this.status = status; return this; }
        public ProductFilterRequestBuilder stockStatus(StockStatus stockStatus) { this.stockStatus = stockStatus; return this; }
        public ProductFilterRequestBuilder prescriptionRequired(Boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; return this; }
        public ProductFilterRequestBuilder featured(Boolean featured) { this.featured = featured; return this; }
        public ProductFilterRequestBuilder bestseller(Boolean bestseller) { this.bestseller = bestseller; return this; }
        public ProductFilterRequestBuilder newArrival(Boolean newArrival) { this.newArrival = newArrival; return this; }
        public ProductFilterRequestBuilder trending(Boolean trending) { this.trending = trending; return this; }
        public ProductFilterRequestBuilder recommended(Boolean recommended) { this.recommended = recommended; return this; }
        public ProductFilterRequestBuilder includeInactive(Boolean includeInactive) { this.includeInactive = includeInactive; return this; }

        public ProductFilterRequest build() {
            return new ProductFilterRequest(search, name, sku, barcode, brandId, categoryId, minPrice, maxPrice, status, stockStatus, prescriptionRequired, featured, bestseller, newArrival, trending, recommended, includeInactive);
        }
    }
}
