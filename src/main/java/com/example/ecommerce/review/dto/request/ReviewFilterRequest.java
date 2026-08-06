package com.example.ecommerce.review.dto.request;

import com.example.ecommerce.review.entity.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Filter criteria for querying product customer reviews.
 */
@Schema(description = "Review search and filter criteria")
public class ReviewFilterRequest {

    @Schema(description = "Filter by Product ID", example = "200")
    private Long productId;

    @Schema(description = "Filter by rating star (1 to 5)", example = "5")
    private Integer rating;

    @Schema(description = "Filter verified purchase reviews only", example = "true")
    private Boolean verifiedOnly;

    @Schema(description = "Filter by review moderation status", example = "APPROVED")
    private ReviewStatus status;

    @Schema(description = "Filter flagged/reported reviews only", example = "false")
    private Boolean isReported;

    @Schema(description = "Keyword search matching title or comment", example = "Quality")
    private String search;

    public ReviewFilterRequest() {
    }

    public ReviewFilterRequest(Long productId, Integer rating, Boolean verifiedOnly, ReviewStatus status, Boolean isReported, String search) {
        this.productId = productId;
        this.rating = rating;
        this.verifiedOnly = verifiedOnly;
        this.status = status;
        this.isReported = isReported;
        this.search = search;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Boolean getVerifiedOnly() { return verifiedOnly; }
    public void setVerifiedOnly(Boolean verifiedOnly) { this.verifiedOnly = verifiedOnly; }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public Boolean getIsReported() { return isReported; }
    public void setIsReported(Boolean isReported) { this.isReported = isReported; }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public static ReviewFilterRequestBuilder builder() { return new ReviewFilterRequestBuilder(); }

    public static class ReviewFilterRequestBuilder {
        private Long productId;
        private Integer rating;
        private Boolean verifiedOnly;
        private ReviewStatus status;
        private Boolean isReported;
        private String search;

        ReviewFilterRequestBuilder() {}

        public ReviewFilterRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public ReviewFilterRequestBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewFilterRequestBuilder verifiedOnly(Boolean verifiedOnly) { this.verifiedOnly = verifiedOnly; return this; }
        public ReviewFilterRequestBuilder status(ReviewStatus status) { this.status = status; return this; }
        public ReviewFilterRequestBuilder isReported(Boolean isReported) { this.isReported = isReported; return this; }
        public ReviewFilterRequestBuilder search(String search) { this.search = search; return this; }

        public ReviewFilterRequest build() {
            return new ReviewFilterRequest(productId, rating, verifiedOnly, status, isReported, search);
        }
    }
}
