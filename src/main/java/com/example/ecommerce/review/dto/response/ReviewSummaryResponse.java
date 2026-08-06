package com.example.ecommerce.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Product rating breakdown summary and statistics projection DTO.
 */
@Schema(description = "Product rating summary statistics")
public class ReviewSummaryResponse {

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Average rating (1.0 to 5.0)", example = "4.5")
    private Double averageRating;

    @Schema(description = "Total approved reviews count", example = "120")
    private Long totalReviews;

    @Schema(description = "1-star rating reviews count", example = "2")
    private Long star1Count;

    @Schema(description = "2-star rating reviews count", example = "3")
    private Long star2Count;

    @Schema(description = "3-star rating reviews count", example = "10")
    private Long star3Count;

    @Schema(description = "4-star rating reviews count", example = "35")
    private Long star4Count;

    @Schema(description = "5-star rating reviews count", example = "70")
    private Long star5Count;

    public ReviewSummaryResponse() {
    }

    public ReviewSummaryResponse(Long productId, Double averageRating, Long totalReviews, Long star1Count, Long star2Count, Long star3Count, Long star4Count, Long star5Count) {
        this.productId = productId;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.star1Count = star1Count;
        this.star2Count = star2Count;
        this.star3Count = star3Count;
        this.star4Count = star4Count;
        this.star5Count = star5Count;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Long totalReviews) { this.totalReviews = totalReviews; }

    public Long getStar1Count() { return star1Count; }
    public void setStar1Count(Long star1Count) { this.star1Count = star1Count; }

    public Long getStar2Count() { return star2Count; }
    public void setStar2Count(Long star2Count) { this.star2Count = star2Count; }

    public Long getStar3Count() { return star3Count; }
    public void setStar3Count(Long star3Count) { this.star3Count = star3Count; }

    public Long getStar4Count() { return star4Count; }
    public void setStar4Count(Long star4Count) { this.star4Count = star4Count; }

    public Long getStar5Count() { return star5Count; }
    public void setStar5Count(Long star5Count) { this.star5Count = star5Count; }

    public static ReviewSummaryResponseBuilder builder() { return new ReviewSummaryResponseBuilder(); }

    public static class ReviewSummaryResponseBuilder {
        private Long productId;
        private Double averageRating;
        private Long totalReviews;
        private Long star1Count;
        private Long star2Count;
        private Long star3Count;
        private Long star4Count;
        private Long star5Count;

        ReviewSummaryResponseBuilder() {}

        public ReviewSummaryResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public ReviewSummaryResponseBuilder averageRating(Double averageRating) { this.averageRating = averageRating; return this; }
        public ReviewSummaryResponseBuilder totalReviews(Long totalReviews) { this.totalReviews = totalReviews; return this; }
        public ReviewSummaryResponseBuilder star1Count(Long star1Count) { this.star1Count = star1Count; return this; }
        public ReviewSummaryResponseBuilder star2Count(Long star2Count) { this.star2Count = star2Count; return this; }
        public ReviewSummaryResponseBuilder star3Count(Long star3Count) { this.star3Count = star3Count; return this; }
        public ReviewSummaryResponseBuilder star4Count(Long star4Count) { this.star4Count = star4Count; return this; }
        public ReviewSummaryResponseBuilder star5Count(Long star5Count) { this.star5Count = star5Count; return this; }

        public ReviewSummaryResponse build() {
            return new ReviewSummaryResponse(productId, averageRating, totalReviews, star1Count, star2Count, star3Count, star4Count, star5Count);
        }
    }
}
