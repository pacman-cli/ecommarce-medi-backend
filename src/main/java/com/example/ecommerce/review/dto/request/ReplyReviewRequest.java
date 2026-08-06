package com.example.ecommerce.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for replying to a customer product review.
 */
@Schema(description = "Payload for merchant/admin review response")
public class ReplyReviewRequest {

    @NotBlank(message = "Reply text is required")
    @Size(max = 1000, message = "Reply text must not exceed 1000 characters")
    @Schema(description = "Merchant or admin reply response text", example = "Thank you for your feedback! We are glad you enjoyed the product.")
    private String replyText;

    public ReplyReviewRequest() {
    }

    public ReplyReviewRequest(String replyText) {
        this.replyText = replyText;
    }

    public String getReplyText() { return replyText; }
    public void setReplyText(String replyText) { this.replyText = replyText; }

    public static ReplyReviewRequestBuilder builder() { return new ReplyReviewRequestBuilder(); }

    public static class ReplyReviewRequestBuilder {
        private String replyText;

        ReplyReviewRequestBuilder() {}

        public ReplyReviewRequestBuilder replyText(String replyText) { this.replyText = replyText; return this; }

        public ReplyReviewRequest build() {
            return new ReplyReviewRequest(replyText);
        }
    }
}
