package com.example.ecommerce.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for reporting an inappropriate or abusive review.
 */
@Schema(description = "Payload for reporting a review")
public class ReportReviewRequest {

    @NotBlank(message = "Reason is required")
    @Size(max = 250, message = "Reason must not exceed 250 characters")
    @Schema(description = "Reason for reporting review", example = "Contains profanity or inappropriate language")
    private String reason;

    public ReportReviewRequest() {
    }

    public ReportReviewRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public static ReportReviewRequestBuilder builder() { return new ReportReviewRequestBuilder(); }

    public static class ReportReviewRequestBuilder {
        private String reason;

        ReportReviewRequestBuilder() {}

        public ReportReviewRequestBuilder reason(String reason) { this.reason = reason; return this; }

        public ReportReviewRequest build() {
            return new ReportReviewRequest(reason);
        }
    }
}
