package com.example.ecommerce.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Result returned by the coupon validation engine.
 */
@Schema(description = "Coupon validation result response")
public class CouponValidationResponse {

    @Schema(description = "Validation success flag", example = "true")
    private boolean valid;

    @Schema(description = "Coupon code evaluated", example = "SAVE20")
    private String couponCode;

    @Schema(description = "Calculated discount amount if valid", example = "15.00")
    private BigDecimal discountAmount;

    @Schema(description = "Validation outcome message or error summary", example = "Coupon applied successfully")
    private String message;

    public CouponValidationResponse() {
    }

    public CouponValidationResponse(boolean valid, String couponCode, BigDecimal discountAmount, String message) {
        this.valid = valid;
        this.couponCode = couponCode;
        this.discountAmount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        this.message = message;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static CouponValidationResponseBuilder builder() { return new CouponValidationResponseBuilder(); }

    public static class CouponValidationResponseBuilder {
        private boolean valid;
        private String couponCode;
        private BigDecimal discountAmount = BigDecimal.ZERO;
        private String message;

        CouponValidationResponseBuilder() {}

        public CouponValidationResponseBuilder valid(boolean valid) { this.valid = valid; return this; }
        public CouponValidationResponseBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public CouponValidationResponseBuilder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public CouponValidationResponseBuilder message(String message) { this.message = message; return this; }

        public CouponValidationResponse build() {
            return new CouponValidationResponse(valid, couponCode, discountAmount, message);
        }
    }
}
