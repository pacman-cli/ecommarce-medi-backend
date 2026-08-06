package com.example.ecommerce.product.entity;

import com.example.ecommerce.brand.entity.Brand;
import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise product entity supporting catalogue information, pricing, stock control,
 * media gallery, medicine specifications, promotional flags and SEO metadata.
 */
@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_products_sku", columnNames = "sku"),
                @UniqueConstraint(name = "uk_products_slug", columnNames = "slug"),
                @UniqueConstraint(name = "uk_products_barcode", columnNames = "barcode")
        }
)
@SQLDelete(sql = "UPDATE products SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Product extends BaseEntity {

    // --- Product Information ---
    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 180)
    private String slug;

    @Column(nullable = false, length = 100)
    private String sku;

    @Column(length = 100)
    private String barcode;

    @Column(name = "generic_name", length = 150)
    private String genericName;

    @Column(length = 150)
    private String manufacturer;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "long_description", columnDefinition = "TEXT")
    private String longDescription;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<ProductImage> images = new ArrayList<>();

    // --- Pricing ---
    @Column(name = "cost_price", precision = 19, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "discount_price", precision = 19, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(precision = 5, scale = 2)
    private BigDecimal tax;

    @Column(length = 10, nullable = false)
    private String currency = "USD";

    // --- Inventory ---
    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "low_stock", nullable = false)
    private Integer lowStock = 5;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", nullable = false, length = 20)
    private StockStatus stockStatus = StockStatus.IN_STOCK;

    // --- Media ---
    @Column(length = 500)
    private String thumbnail;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    // --- Medicine Information ---
    @Column(name = "prescription_required", nullable = false)
    private boolean prescriptionRequired = false;

    @Column(name = "dosage_form", length = 100)
    private String dosageForm;

    @Column(length = 100)
    private String strength;

    @Column(name = "pack_size", length = 100)
    private String packSize;

    @Column(name = "storage_condition", length = 250)
    private String storageCondition;

    @Column(columnDefinition = "TEXT")
    private String warnings;

    @Column(name = "side_effects", columnDefinition = "TEXT")
    private String sideEffects;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    // --- Product Flags ---
    @Column(nullable = false)
    private boolean featured = false;

    @Column(nullable = false)
    private boolean bestseller = false;

    @Column(name = "new_arrival", nullable = false)
    private boolean newArrival = false;

    @Column(nullable = false)
    private boolean trending = false;

    @Column(nullable = false)
    private boolean recommended = false;

    // --- SEO ---
    @Column(name = "meta_title", length = 150)
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(length = 300)
    private String keywords;

    // --- Status & Soft Delete ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Product() {
    }

    public void addImage(ProductImage image) {
        if (image != null) {
            if (this.images == null) {
                this.images = new ArrayList<>();
            }
            this.images.add(image);
            image.setProduct(this);
        }
    }

    public void removeImage(ProductImage image) {
        if (image != null && this.images != null) {
            this.images.remove(image);
            image.setProduct(null);
        }
    }

    public void recalculateStockStatus() {
        int available = (quantity != null ? quantity : 0) - (reservedQuantity != null ? reservedQuantity : 0);
        int threshold = lowStock != null ? lowStock : 5;
        if (available <= 0) {
            this.stockStatus = StockStatus.OUT_OF_STOCK;
        } else if (available <= threshold) {
            this.stockStatus = StockStatus.LOW_STOCK;
        } else {
            this.stockStatus = StockStatus.IN_STOCK;
        }
    }

    // Getters and Setters
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

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }

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

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static ProductBuilder builder() { return new ProductBuilder(); }

    public static class ProductBuilder {
        private String name;
        private String slug;
        private String sku;
        private String barcode;
        private String genericName;
        private String manufacturer;
        private String shortDescription;
        private String longDescription;
        private Brand brand;
        private Category category;
        private List<ProductImage> images = new ArrayList<>();
        private BigDecimal costPrice;
        private BigDecimal sellingPrice;
        private BigDecimal discountPrice;
        private BigDecimal discountPercentage;
        private BigDecimal tax;
        private String currency = "USD";
        private Integer quantity = 0;
        private Integer lowStock = 5;
        private Integer reservedQuantity = 0;
        private StockStatus stockStatus = StockStatus.IN_STOCK;
        private String thumbnail;
        private String videoUrl;
        private boolean prescriptionRequired = false;
        private String dosageForm;
        private String strength;
        private String packSize;
        private String storageCondition;
        private String warnings;
        private String sideEffects;
        private String ingredients;
        private boolean featured = false;
        private boolean bestseller = false;
        private boolean newArrival = false;
        private boolean trending = false;
        private boolean recommended = false;
        private String metaTitle;
        private String metaDescription;
        private String keywords;
        private ProductStatus status = ProductStatus.ACTIVE;
        private boolean active = true;
        private boolean deleted = false;
        private Instant deletedAt;

        ProductBuilder() {}

        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder slug(String slug) { this.slug = slug; return this; }
        public ProductBuilder sku(String sku) { this.sku = sku; return this; }
        public ProductBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public ProductBuilder genericName(String genericName) { this.genericName = genericName; return this; }
        public ProductBuilder manufacturer(String manufacturer) { this.manufacturer = manufacturer; return this; }
        public ProductBuilder shortDescription(String shortDescription) { this.shortDescription = shortDescription; return this; }
        public ProductBuilder longDescription(String longDescription) { this.longDescription = longDescription; return this; }
        public ProductBuilder brand(Brand brand) { this.brand = brand; return this; }
        public ProductBuilder category(Category category) { this.category = category; return this; }
        public ProductBuilder images(List<ProductImage> images) { this.images = images; return this; }
        public ProductBuilder costPrice(BigDecimal costPrice) { this.costPrice = costPrice; return this; }
        public ProductBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public ProductBuilder discountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; return this; }
        public ProductBuilder discountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; return this; }
        public ProductBuilder tax(BigDecimal tax) { this.tax = tax; return this; }
        public ProductBuilder currency(String currency) { this.currency = currency; return this; }
        public ProductBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public ProductBuilder lowStock(Integer lowStock) { this.lowStock = lowStock; return this; }
        public ProductBuilder reservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public ProductBuilder stockStatus(StockStatus stockStatus) { this.stockStatus = stockStatus; return this; }
        public ProductBuilder thumbnail(String thumbnail) { this.thumbnail = thumbnail; return this; }
        public ProductBuilder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public ProductBuilder prescriptionRequired(boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; return this; }
        public ProductBuilder dosageForm(String dosageForm) { this.dosageForm = dosageForm; return this; }
        public ProductBuilder strength(String strength) { this.strength = strength; return this; }
        public ProductBuilder packSize(String packSize) { this.packSize = packSize; return this; }
        public ProductBuilder storageCondition(String storageCondition) { this.storageCondition = storageCondition; return this; }
        public ProductBuilder warnings(String warnings) { this.warnings = warnings; return this; }
        public ProductBuilder sideEffects(String sideEffects) { this.sideEffects = sideEffects; return this; }
        public ProductBuilder ingredients(String ingredients) { this.ingredients = ingredients; return this; }
        public ProductBuilder featured(boolean featured) { this.featured = featured; return this; }
        public ProductBuilder bestseller(boolean bestseller) { this.bestseller = bestseller; return this; }
        public ProductBuilder newArrival(boolean newArrival) { this.newArrival = newArrival; return this; }
        public ProductBuilder trending(boolean trending) { this.trending = trending; return this; }
        public ProductBuilder recommended(boolean recommended) { this.recommended = recommended; return this; }
        public ProductBuilder metaTitle(String metaTitle) { this.metaTitle = metaTitle; return this; }
        public ProductBuilder metaDescription(String metaDescription) { this.metaDescription = metaDescription; return this; }
        public ProductBuilder keywords(String keywords) { this.keywords = keywords; return this; }
        public ProductBuilder status(ProductStatus status) { this.status = status; return this; }
        public ProductBuilder active(boolean active) { this.active = active; return this; }
        public ProductBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public ProductBuilder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }

        public Product build() {
            Product p = new Product();
            p.setName(name);
            p.setSlug(slug);
            p.setSku(sku);
            p.setBarcode(barcode);
            p.setGenericName(genericName);
            p.setManufacturer(manufacturer);
            p.setShortDescription(shortDescription);
            p.setLongDescription(longDescription);
            p.setBrand(brand);
            p.setCategory(category);
            p.setImages(images != null ? images : new ArrayList<>());
            p.setCostPrice(costPrice);
            p.setSellingPrice(sellingPrice);
            p.setDiscountPrice(discountPrice);
            p.setDiscountPercentage(discountPercentage);
            p.setTax(tax);
            p.setCurrency(currency != null ? currency : "USD");
            p.setQuantity(quantity != null ? quantity : 0);
            p.setLowStock(lowStock != null ? lowStock : 5);
            p.setReservedQuantity(reservedQuantity != null ? reservedQuantity : 0);
            p.setStockStatus(stockStatus != null ? stockStatus : StockStatus.IN_STOCK);
            p.setThumbnail(thumbnail);
            p.setVideoUrl(videoUrl);
            p.setPrescriptionRequired(prescriptionRequired);
            p.setDosageForm(dosageForm);
            p.setStrength(strength);
            p.setPackSize(packSize);
            p.setStorageCondition(storageCondition);
            p.setWarnings(warnings);
            p.setSideEffects(sideEffects);
            p.setIngredients(ingredients);
            p.setFeatured(featured);
            p.setBestseller(bestseller);
            p.setNewArrival(newArrival);
            p.setTrending(trending);
            p.setRecommended(recommended);
            p.setMetaTitle(metaTitle);
            p.setMetaDescription(metaDescription);
            p.setKeywords(keywords);
            p.setStatus(status != null ? status : ProductStatus.ACTIVE);
            p.setActive(active);
            p.setDeleted(deleted);
            p.setDeletedAt(deletedAt);
            return p;
        }
    }
}
