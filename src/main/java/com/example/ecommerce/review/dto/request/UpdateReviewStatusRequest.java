package com.example.ecommerce.review.dto.request;

import com.example.ecommerce.review.entity.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for updating review moderation status.
 */
@Schema(description = "Payload for review moderation status update")
public class UpdateReviewStatusRequest {

    @NotNull(message = "Review status is required")
    @Schema(description = "Target review status", example = "APPROVED")
    private ReviewStatus status;

    @Size(max = 250, message = "Reason must not exceed 250 characters")
    @Schema(description = "Moderation reason", example = "Review meets community guidelines")
    private String reason;

    public UpdateReviewStatusRequest() {
    }

    public UpdateReviewStatusRequest(ReviewStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public static UpdateReviewStatusRequestBuilder builder() { return new UpdateReviewStatusRequestBuilder(); }

    public static class UpdateReviewStatusRequestBuilder {
        private ReviewStatus status;
        private String reason;

        UpdateReviewStatusRequestBuilder() {}

        public UpdateReviewStatusRequestBuilder status(ReviewStatus status) { this.status = status; return this; }
        public UpdateReviewStatusRequestBuilder reason(String reason) { this.reason = reason; return this; }

        public UpdateReviewStatusRequest build() {
            return new UpdateReviewStatusRequest(status, reason);
        }
    }
}
