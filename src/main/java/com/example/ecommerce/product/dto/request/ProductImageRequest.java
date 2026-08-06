package com.example.ecommerce.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for adding or updating a product image.
 */
@Schema(description = "Payload for product gallery image")
public class ProductImageRequest {

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Schema(description = "Product image URL", example = "https://images.example.com/products/phone-front.jpg")
    private String imageUrl;

    @Size(max = 200, message = "Alt text must not exceed 200 characters")
    @Schema(description = "Accessible alt text", example = "Smartphone Front View")
    private String altText;

    @Min(value = 0, message = "Display order must be non-negative")
    @Schema(description = "Display priority order", example = "1")
    private Integer displayOrder;

    @Schema(description = "Indicates thumbnail image", example = "false")
    private Boolean isThumbnail;

    public ProductImageRequest() {
    }

    public ProductImageRequest(String imageUrl, String altText, Integer displayOrder, Boolean isThumbnail) {
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.displayOrder = displayOrder;
        this.isThumbnail = isThumbnail;
    }

    public static ProductImageRequestBuilder builder() {
        return new ProductImageRequestBuilder();
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getIsThumbnail() { return isThumbnail; }
    public void setIsThumbnail(Boolean isThumbnail) { this.isThumbnail = isThumbnail; }

    public static class ProductImageRequestBuilder {
        private String imageUrl;
        private String altText;
        private Integer displayOrder;
        private Boolean isThumbnail;

        ProductImageRequestBuilder() {}

        public ProductImageRequestBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductImageRequestBuilder altText(String altText) { this.altText = altText; return this; }
        public ProductImageRequestBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }
        public ProductImageRequestBuilder isThumbnail(Boolean isThumbnail) { this.isThumbnail = isThumbnail; return this; }

        public ProductImageRequest build() {
            return new ProductImageRequest(imageUrl, altText, displayOrder, isThumbnail);
        }
    }
}
