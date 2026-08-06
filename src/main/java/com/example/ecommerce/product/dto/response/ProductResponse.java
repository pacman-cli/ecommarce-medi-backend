package com.example.ecommerce.product.dto.response;

import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Detailed product response DTO.
 */
@Schema(description = "Product details response")
public class ProductResponse {

    @Schema(description = "Product ID", example = "100")
    private Long id;

    @Schema(description = "Product name", example = "Paracetamol 500mg Tablets")
    private String name;

    @Schema(description = "URL slug", example = "paracetamol-500mg-tablets")
    private String slug;

    @Schema(description = "SKU", example = "MED-PARA-500")
    private String sku;

    @Schema(description = "Barcode", example = "8901234567890")
    private String barcode;

    @Schema(description = "Generic name", example = "Paracetamol")
    private String genericName;

    @Schema(description = "Manufacturer name", example = "PharmaCare Labs")
    private String manufacturer;

    @Schema(description = "Short description", example = "Fast pain relief tablets.")
    private String shortDescription;

    @Schema(description = "Long description")
    private String longDescription;

    // Brand context
    @Schema(description = "Brand ID", example = "5")
    private Long brandId;

    @Schema(description = "Brand name", example = "PharmaBrand")
    private String brandName;

    @Schema(description = "Brand slug", example = "pharmabrand")
    private String brandSlug;

    // Category context
    @Schema(description = "Category ID", example = "12")
    private Long categoryId;

    @Schema(description = "Category name", example = "Pain Relief")
    private String categoryName;

    @Schema(description = "Category slug", example = "pain-relief")
    private String categorySlug;

    // Pricing
    @Schema(description = "Cost price", example = "3.50")
    private BigDecimal costPrice;

    @Schema(description = "Selling price", example = "5.99")
    private BigDecimal sellingPrice;

    @Schema(description = "Discount price", example = "4.99")
    private BigDecimal discountPrice;

    @Schema(description = "Discount percentage", example = "16.69")
    private BigDecimal discountPercentage;

    @Schema(description = "Tax percentage", example = "5.00")
    private BigDecimal tax;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    // Inventory
    @Schema(description = "Total stock quantity", example = "150")
    private Integer quantity;

    @Schema(description = "Low stock threshold", example = "10")
    private Integer lowStock;

    @Schema(description = "Reserved quantity", example = "5")
    private Integer reservedQuantity;

    @Schema(description = "Stock status", example = "IN_STOCK")
    private StockStatus stockStatus;

    // Media
    @Schema(description = "Thumbnail image URL", example = "https://images.example.com/products/thumb-para.jpg")
    private String thumbnail;

    @Schema(description = "Video URL", example = "https://youtube.com/watch?v=123")
    private String videoUrl;

    @Schema(description = "Gallery images")
    private List<ProductImageResponse> images;

    // Medicine Information
    @Schema(description = "Prescription required flag", example = "false")
    private boolean prescriptionRequired;

    @Schema(description = "Dosage form", example = "Tablet")
    private String dosageForm;

    @Schema(description = "Active strength", example = "500mg")
    private String strength;

    @Schema(description = "Pack size", example = "10x10 Blister Pack")
    private String packSize;

    @Schema(description = "Storage condition", example = "Store below 25°C.")
    private String storageCondition;

    @Schema(description = "Safety warnings")
    private String warnings;

    @Schema(description = "Side effects")
    private String sideEffects;

    @Schema(description = "Active ingredients")
    private String ingredients;

    // Flags
    @Schema(description = "Featured flag", example = "true")
    private boolean featured;

    @Schema(description = "Bestseller flag", example = "true")
    private boolean bestseller;

    @Schema(description = "New arrival flag", example = "false")
    private boolean newArrival;

    @Schema(description = "Trending flag", example = "true")
    private boolean trending;

    @Schema(description = "Recommended flag", example = "true")
    private boolean recommended;

    // SEO
    @Schema(description = "SEO Meta title")
    private String metaTitle;

    @Schema(description = "SEO Meta description")
    private String metaDescription;

    @Schema(description = "Search keywords")
    private String keywords;

    // Status & Audit
    @Schema(description = "Product status", example = "ACTIVE")
    private ProductStatus status;

    @Schema(description = "Active indicator", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    private Instant updatedAt;

    @Schema(description = "Created by user", example = "admin@example.com")
    private String createdBy;

    @Schema(description = "Updated by user", example = "admin@example.com")
    private String updatedBy;

    @Schema(description = "Optimistic lock version", example = "0")
    private Long version;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String slug, String sku, String barcode, String genericName,
                           String manufacturer, String shortDescription, String longDescription, Long brandId,
                           String brandName, String brandSlug, Long categoryId, String categoryName, String categorySlug,
                           BigDecimal costPrice, BigDecimal sellingPrice, BigDecimal discountPrice, BigDecimal discountPercentage,
                           BigDecimal tax, String currency, Integer quantity, Integer lowStock, Integer reservedQuantity,
                           StockStatus stockStatus, String thumbnail, String videoUrl, List<ProductImageResponse> images,
                           boolean prescriptionRequired, String dosageForm, String strength, String packSize,
                           String storageCondition, String warnings, String sideEffects, String ingredients,
                           boolean featured, boolean bestseller, boolean newArrival, boolean trending, boolean recommended,
                           String metaTitle, String metaDescription, String keywords, ProductStatus status, boolean active,
                           Instant createdAt, Instant updatedAt, String createdBy, String updatedBy, Long version) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.barcode = barcode;
        this.genericName = genericName;
        this.manufacturer = manufacturer;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandSlug = brandSlug;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.discountPrice = discountPrice;
        this.discountPercentage = discountPercentage;
        this.tax = tax;
        this.currency = currency;
        this.quantity = quantity;
        this.lowStock = lowStock;
        this.reservedQuantity = reservedQuantity;
        this.stockStatus = stockStatus;
        this.thumbnail = thumbnail;
        this.videoUrl = videoUrl;
        this.images = images;
        this.prescriptionRequired = prescriptionRequired;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.packSize = packSize;
        this.storageCondition = storageCondition;
        this.warnings = warnings;
        this.sideEffects = sideEffects;
        this.ingredients = ingredients;
        this.featured = featured;
        this.bestseller = bestseller;
        this.newArrival = newArrival;
        this.trending = trending;
        this.recommended = recommended;
        this.metaTitle = metaTitle;
        this.metaDescription = metaDescription;
        this.keywords = keywords;
        this.status = status;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.version = version;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getBrandSlug() { return brandSlug; }
    public void setBrandSlug(String brandSlug) { this.brandSlug = brandSlug; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategorySlug() { return categorySlug; }
    public void setCategorySlug(String categorySlug) { this.categorySlug = categorySlug; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; }

    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getLowStock() { return lowStock; }
    public void setLowStock(Integer lowStock) { this.lowStock = lowStock; }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public StockStatus getStockStatus() { return stockStatus; }
    public void setStockStatus(StockStatus stockStatus) { this.stockStatus = stockStatus; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public List<ProductImageResponse> getImages() { return images; }
    public void setImages(List<ProductImageResponse> images) { this.images = images; }

    public boolean isPrescriptionRequired() { return prescriptionRequired; }
    public void setPrescriptionRequired(boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; }

    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }

    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }

    public String getPackSize() { return packSize; }
    public void setPackSize(String packSize) { this.packSize = packSize; }

    public String getStorageCondition() { return storageCondition; }
    public void setStorageCondition(String storageCondition) { this.storageCondition = storageCondition; }

    public String getWarnings() { return warnings; }
    public void setWarnings(String warnings) { this.warnings = warnings; }

    public String getSideEffects() { return sideEffects; }
    public void setSideEffects(String sideEffects) { this.sideEffects = sideEffects; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public boolean isBestseller() { return bestseller; }
    public void setBestseller(boolean bestseller) { this.bestseller = bestseller; }

    public boolean isNewArrival() { return newArrival; }
    public void setNewArrival(boolean newArrival) { this.newArrival = newArrival; }

    public boolean isTrending() { return trending; }
    public void setTrending(boolean trending) { this.trending = trending; }

    public boolean isRecommended() { return recommended; }
    public void setRecommended(boolean recommended) { this.recommended = recommended; }

    public String getMetaTitle() { return metaTitle; }
    public void setMetaTitle(String metaTitle) { this.metaTitle = metaTitle; }

    public String getMetaDescription() { return metaDescription; }
    public void setMetaDescription(String metaDescription) { this.metaDescription = metaDescription; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public static ProductResponseBuilder builder() { return new ProductResponseBuilder(); }

    public static class ProductResponseBuilder {
        private Long id;
        private String name;
        private String slug;
        private String sku;
        private String barcode;
        private String genericName;
        private String manufacturer;
        private String shortDescription;
        private String longDescription;
        private Long brandId;
        private String brandName;
        private String brandSlug;
        private Long categoryId;
        private String categoryName;
        private String categorySlug;
        private BigDecimal costPrice;
        private BigDecimal sellingPrice;
        private BigDecimal discountPrice;
        private BigDecimal discountPercentage;
        private BigDecimal tax;
        private String currency;
        private Integer quantity;
        private Integer lowStock;
        private Integer reservedQuantity;
        private StockStatus stockStatus;
        private String thumbnail;
        private String videoUrl;
        private List<ProductImageResponse> images;
        private boolean prescriptionRequired;
        private String dosageForm;
        private String strength;
        private String packSize;
        private String storageCondition;
        private String warnings;
        private String sideEffects;
        private String ingredients;
        private boolean featured;
        private boolean bestseller;
        private boolean newArrival;
        private boolean trending;
        private boolean recommended;
        private String metaTitle;
        private String metaDescription;
        private String keywords;
        private ProductStatus status;
        private boolean active;
        private Instant createdAt;
        private Instant updatedAt;
        private String createdBy;
        private String updatedBy;
        private Long version;

        ProductResponseBuilder() {}

        public ProductResponseBuilder id(Long id) { this.id = id; return this; }
        public ProductResponseBuilder name(String name) { this.name = name; return this; }
        public ProductResponseBuilder slug(String slug) { this.slug = slug; return this; }
        public ProductResponseBuilder sku(String sku) { this.sku = sku; return this; }
        public ProductResponseBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public ProductResponseBuilder genericName(String genericName) { this.genericName = genericName; return this; }
        public ProductResponseBuilder manufacturer(String manufacturer) { this.manufacturer = manufacturer; return this; }
        public ProductResponseBuilder shortDescription(String shortDescription) { this.shortDescription = shortDescription; return this; }
        public ProductResponseBuilder longDescription(String longDescription) { this.longDescription = longDescription; return this; }
        public ProductResponseBuilder brandId(Long brandId) { this.brandId = brandId; return this; }
        public ProductResponseBuilder brandName(String brandName) { this.brandName = brandName; return this; }
        public ProductResponseBuilder brandSlug(String brandSlug) { this.brandSlug = brandSlug; return this; }
        public ProductResponseBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public ProductResponseBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public ProductResponseBuilder categorySlug(String categorySlug) { this.categorySlug = categorySlug; return this; }
        public ProductResponseBuilder costPrice(BigDecimal costPrice) { this.costPrice = costPrice; return this; }
        public ProductResponseBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public ProductResponseBuilder discountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; return this; }
        public ProductResponseBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public ProductResponseBuilder tax(BigDecimal tax) { this.tax = tax; return this; }
        public ProductResponseBuilder currency(String currency) { this.currency = currency; return this; }
        public ProductResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public ProductResponseBuilder lowStock(Integer lowStock) { this.lowStock = lowStock; return this; }
        public ProductResponseBuilder reservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public ProductResponseBuilder stockStatus(StockStatus stockStatus) { this.stockStatus = stockStatus; return this; }
        public ProductResponseBuilder thumbnail(String thumbnail) { this.thumbnail = thumbnail; return this; }
        public ProductResponseBuilder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public ProductResponseBuilder images(List<ProductImageResponse> images) { this.images = images; return this; }
        public ProductResponseBuilder prescriptionRequired(boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; return this; }
        public ProductResponseBuilder dosageForm(String dosageForm) { this.dosageForm = dosageForm; return this; }
        public ProductResponseBuilder strength(String strength) { this.strength = strength; return this; }
        public ProductResponseBuilder packSize(String packSize) { this.packSize = packSize; return this; }
        public ProductResponseBuilder storageCondition(String storageCondition) { this.storageCondition = storageCondition; return this; }
        public ProductResponseBuilder warnings(String warnings) { this.warnings = warnings; return this; }
        public ProductResponseBuilder sideEffects(String sideEffects) { this.sideEffects = sideEffects; return this; }
        public ProductResponseBuilder ingredients(String ingredients) { this.ingredients = ingredients; return this; }
        public ProductResponseBuilder featured(boolean featured) { this.featured = featured; return this; }
        public ProductResponseBuilder bestseller(boolean bestseller) { this.bestseller = bestseller; return this; }
        public ProductResponseBuilder newArrival(boolean newArrival) { this.newArrival = newArrival; return this; }
        public ProductResponseBuilder trending(boolean trending) { this.trending = trending; return this; }
        public ProductResponseBuilder recommended(boolean recommended) { this.recommended = recommended; return this; }
        public ProductResponseBuilder metaTitle(String metaTitle) { this.metaTitle = metaTitle; return this; }
        public ProductResponseBuilder metaDescription(String metaDescription) { this.metaDescription = metaDescription; return this; }
        public ProductResponseBuilder keywords(String keywords) { this.keywords = keywords; return this; }
        public ProductResponseBuilder status(ProductStatus status) { this.status = status; return this; }
        public ProductResponseBuilder active(boolean active) { this.active = active; return this; }
        public ProductResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ProductResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public ProductResponseBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public ProductResponseBuilder updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }
        public ProductResponseBuilder version(Long version) { this.version = version; return this; }

        public ProductResponse build() {
            return new ProductResponse(id, name, slug, sku, barcode, genericName, manufacturer, shortDescription, longDescription, brandId, brandName, brandSlug, categoryId, categoryName, categorySlug, costPrice, sellingPrice, discountPrice, discountPercentage, tax, currency, quantity, lowStock, reservedQuantity, stockStatus, thumbnail, videoUrl, images, prescriptionRequired, dosageForm, strength, packSize, storageCondition, warnings, sideEffects, ingredients, featured, bestseller, newArrival, trending, recommended, metaTitle, metaDescription, keywords, status, active, createdAt, updatedAt, createdBy, updatedBy, version);
        }
    }
}
