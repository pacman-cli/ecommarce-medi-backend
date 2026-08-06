package com.example.ecommerce.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response projection for product gallery images.
 */
@Schema(description = "Product image response")
public class ProductImageResponse {

    @Schema(description = "Image ID", example = "10")
    private Long id;

    @Schema(description = "Image URL", example = "https://images.example.com/products/phone-front.jpg")
    private String imageUrl;

    @Schema(description = "Alt text", example = "Smartphone Front View")
    private String altText;

    @Schema(description = "Display order", example = "1")
    private Integer displayOrder;

    @Schema(description = "Thumbnail flag", example = "true")
    private boolean isThumbnail;

    public ProductImageResponse() {
    }

    public ProductImageResponse(Long id, String imageUrl, String altText, Integer displayOrder, boolean isThumbnail) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.displayOrder = displayOrder;
        this.isThumbnail = isThumbnail;
    }

    public static ProductImageResponseBuilder builder() {
        return new ProductImageResponseBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public boolean isThumbnail() { return isThumbnail; }
    public void setThumbnail(boolean thumbnail) { isThumbnail = thumbnail; }

    public static class ProductImageResponseBuilder {
        private Long id;
        private String imageUrl;
        private String altText;
        private Integer displayOrder;
        private boolean isThumbnail;

        ProductImageResponseBuilder() {}

        public ProductImageResponseBuilder id(Long id) { this.id = id; return this; }
        public ProductImageResponseBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductImageResponseBuilder altText(String altText) { this.altText = altText; return this; }
        public ProductImageResponseBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }
        public ProductImageResponseBuilder isThumbnail(boolean isThumbnail) { this.isThumbnail = isThumbnail; return this; }

        public ProductImageResponse build() {
            return new ProductImageResponse(id, imageUrl, altText, displayOrder, isThumbnail);
        }
    }
}
