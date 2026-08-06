package com.example.ecommerce.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Image response DTO for review photo attachments.
 */
@Schema(description = "Review image attachment response")
public class ReviewImageResponse {

    @Schema(description = "Image ID", example = "10")
    private Long id;

    @Schema(description = "Image URL", example = "https://images.example.com/reviews/photo1.jpg")
    private String imageUrl;

    @Schema(description = "Display sort order", example = "0")
    private Integer sortOrder;

    public ReviewImageResponse() {
    }

    public ReviewImageResponse(Long id, String imageUrl, Integer sortOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public static ReviewImageResponseBuilder builder() { return new ReviewImageResponseBuilder(); }

    public static class ReviewImageResponseBuilder {
        private Long id;
        private String imageUrl;
        private Integer sortOrder;

        ReviewImageResponseBuilder() {}

        public ReviewImageResponseBuilder id(Long id) { this.id = id; return this; }
        public ReviewImageResponseBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ReviewImageResponseBuilder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }

        public ReviewImageResponse build() {
            return new ReviewImageResponse(id, imageUrl, sortOrder);
        }
    }
}
