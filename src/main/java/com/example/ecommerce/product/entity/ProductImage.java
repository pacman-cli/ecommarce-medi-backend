package com.example.ecommerce.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Product media image gallery item.
 */
@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "alt_text", length = 200)
    private String altText;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "is_thumbnail", nullable = false)
    private boolean isThumbnail = false;

    public ProductImage() {
    }

    public ProductImage(Long id, Product product, String imageUrl, String altText, Integer displayOrder, boolean isThumbnail) {
        this.id = id;
        this.product = product;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.isThumbnail = isThumbnail;
    }

    public static ProductImageBuilder builder() {
        return new ProductImageBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        isThumbnail = thumbnail;
    }

    public static class ProductImageBuilder {
        private Long id;
        private Product product;
        private String imageUrl;
        private String altText;
        private Integer displayOrder = 0;
        private boolean isThumbnail = false;

        ProductImageBuilder() {
        }

        public ProductImageBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductImageBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public ProductImageBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductImageBuilder altText(String altText) {
            this.altText = altText;
            return this;
        }

        public ProductImageBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public ProductImageBuilder isThumbnail(boolean isThumbnail) {
            this.isThumbnail = isThumbnail;
            return this;
        }

        public ProductImage build() {
            return new ProductImage(id, product, imageUrl, altText, displayOrder, isThumbnail);
        }
    }
}
