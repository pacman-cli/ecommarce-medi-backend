package com.example.ecommerce.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Payload for validating a coupon code against a cart subtotal at checkout.
 */
@Schema(description = "Payload for coupon validation engine")
public class ValidateCouponRequest {

    @NotBlank(message = "Coupon code is required")
    @Schema(description = "Promotional coupon code to validate", example = "SAVE20")
    private String couponCode;

    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", message = "Subtotal must be non-negative")
    @Schema(description = "Gross cart subtotal before discount", example = "100.00")
    private BigDecimal subtotal;

    public ValidateCouponRequest() {
    }

    public ValidateCouponRequest(String couponCode, BigDecimal subtotal) {
        this.couponCode = couponCode;
        this.subtotal = subtotal;
    }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public static ValidateCouponRequestBuilder builder() { return new ValidateCouponRequestBuilder(); }

    public static class ValidateCouponRequestBuilder {
        private String couponCode;
        private BigDecimal subtotal;

        ValidateCouponRequestBuilder() {}

        public ValidateCouponRequestBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public ValidateCouponRequestBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }

        public ValidateCouponRequest build() {
            return new ValidateCouponRequest(couponCode, subtotal);
        }
    }
}
