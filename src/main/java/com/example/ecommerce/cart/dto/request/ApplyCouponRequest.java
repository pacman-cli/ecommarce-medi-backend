package com.example.ecommerce.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for applying a promotional coupon code to cart.
 */
@Schema(description = "Payload for applying coupon code")
public class ApplyCouponRequest {

    @NotBlank(message = "Coupon code is required")
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    @Schema(description = "Promotional coupon code", example = "SAVE10")
    private String couponCode;

    public ApplyCouponRequest() {
    }

    public ApplyCouponRequest(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public static ApplyCouponRequestBuilder builder() { return new ApplyCouponRequestBuilder(); }

    public static class ApplyCouponRequestBuilder {
        private String couponCode;

        ApplyCouponRequestBuilder() {}

        public ApplyCouponRequestBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }

        public ApplyCouponRequest build() {
            return new ApplyCouponRequest(couponCode);
        }
    }
}
