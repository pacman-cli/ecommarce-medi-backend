package com.example.ecommerce.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for merging a guest session cart into an authenticated user's cart.
 */
@Schema(description = "Payload for merging guest cart into user cart")
public class MergeCartRequest {

    @NotBlank(message = "Guest session ID is required")
    @Size(max = 100, message = "Guest session ID must not exceed 100 characters")
    @Schema(description = "Guest session ID", example = "guest-session-12345")
    private String guestSessionId;

    public MergeCartRequest() {
    }

    public MergeCartRequest(String guestSessionId) {
        this.guestSessionId = guestSessionId;
    }

    public String getGuestSessionId() { return guestSessionId; }
    public void setGuestSessionId(String guestSessionId) { this.guestSessionId = guestSessionId; }

    public static MergeCartRequestBuilder builder() { return new MergeCartRequestBuilder(); }

    public static class MergeCartRequestBuilder {
        private String guestSessionId;

        MergeCartRequestBuilder() {}

        public MergeCartRequestBuilder guestSessionId(String guestSessionId) { this.guestSessionId = guestSessionId; return this; }

        public MergeCartRequest build() {
            return new MergeCartRequest(guestSessionId);
        }
    }
}
