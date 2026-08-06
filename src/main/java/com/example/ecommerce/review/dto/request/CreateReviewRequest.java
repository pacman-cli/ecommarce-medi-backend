package com.example.ecommerce.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Payload for submitting a customer review for a product.
 */
@Schema(description = "Payload for submitting a product review")
public class CreateReviewRequest {

    @NotNull(message = "Product ID is required")
    @Schema(description = "Target product ID", example = "200")
    private Long productId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1 star")
    @Max(value = 5, message = "Rating cannot exceed 5 stars")
    @Schema(description = "Rating stars count (1 to 5)", example = "5")
    private Integer rating;

    @Size(max = 150, message = "Headline title must not exceed 150 characters")
    @Schema(description = "Review title headline", example = "Excellent quality product!")
    private String title;

    @NotBlank(message = "Review comment is required")
    @Size(max = 2000, message = "Review comment must not exceed 2000 characters")
    @Schema(description = "Detailed review comment", example = "Fast delivery and great quality medicine. Highly recommend.")
    private String comment;

    @Schema(description = "Optional list of attached review photo URLs")
    private List<String> imageUrls;

    public CreateReviewRequest() {
    }

    public CreateReviewRequest(Long productId, Integer rating, String title, String comment, List<String> imageUrls) {
        this.productId = productId;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.imageUrls = imageUrls;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public static CreateReviewRequestBuilder builder() { return new CreateReviewRequestBuilder(); }

    public static class CreateReviewRequestBuilder {
        private Long productId;
        private Integer rating;
        private String title;
        private String comment;
        private List<String> imageUrls;

        CreateReviewRequestBuilder() {}

        public CreateReviewRequestBuilder productId(Long productId) { this.productId = productId; return this; }
        public CreateReviewRequestBuilder rating(Integer rating) { this.rating = rating; return this; }
        public CreateReviewRequestBuilder title(String title) { this.title = title; return this; }
        public CreateReviewRequestBuilder comment(String comment) { this.comment = comment; return this; }
        public CreateReviewRequestBuilder imageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; return this; }

        public CreateReviewRequest build() {
            return new CreateReviewRequest(productId, rating, title, comment, imageUrls);
        }
    }
}
