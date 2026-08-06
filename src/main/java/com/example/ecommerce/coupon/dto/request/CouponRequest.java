package com.example.ecommerce.coupon.dto.request;

import com.example.ecommerce.coupon.entity.CouponStatus;
import com.example.ecommerce.coupon.entity.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Payload for creating or updating a promotional coupon.
 */
@Schema(description = "Payload for creating or updating a coupon")
public class CouponRequest {

    @NotBlank(message = "Coupon code is required")
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Coupon code must contain only uppercase alphanumeric characters, hyphens or underscores")
    @Schema(description = "Unique uppercase coupon code", example = "SAVE20")
    private String code;

    @Size(max = 250, message = "Description must not exceed 250 characters")
    @Schema(description = "Coupon description", example = "20% off on all pharmaceutical orders")
    private String description;

    @NotNull(message = "Discount type is required")
    @Schema(description = "Discount type (PERCENTAGE or FIXED_AMOUNT)", example = "PERCENTAGE")
    private DiscountType discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    @Schema(description = "Discount value (percentage e.g. 20 or fixed amount e.g. 5.00)", example = "20.00")
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "Minimum purchase amount must be non-negative")
    @Schema(description = "Minimum cart subtotal required to redeem", example = "50.00")
    private BigDecimal minPurchaseAmount;

    @DecimalMin(value = "0.0", message = "Maximum discount amount must be non-negative")
    @Schema(description = "Maximum monetary cap for percentage discounts", example = "15.00")
    private BigDecimal maxDiscountAmount;

    @Schema(description = "Activation start timestamp", example = "2026-08-01T00:00:00Z")
    private Instant startDate;

    @Schema(description = "Expiration end timestamp", example = "2026-12-31T23:59:59Z")
    private Instant endDate;

    @Min(value = 1, message = "Global usage limit must be at least 1")
    @Schema(description = "Total global redemption limit across all users", example = "1000")
    private Integer usageLimit;

    @Min(value = 1, message = "Per-user limit must be at least 1")
    @Schema(description = "Maximum redemptions allowed per individual user", example = "1")
    private Integer perUserLimit;

    @Schema(description = "Indicates whether coupon applies automatically at checkout", example = "false")
    private Boolean isAutomatic;

    @Schema(description = "Coupon operational status", example = "ACTIVE")
    private CouponStatus status;

    @Schema(description = "Active operational flag", example = "true")
    private Boolean active;

    public CouponRequest() {
    }

    public CouponRequest(String code, String description, DiscountType discountType, BigDecimal discountValue, BigDecimal minPurchaseAmount, BigDecimal maxDiscountAmount, Instant startDate, Instant endDate, Integer usageLimit, Integer perUserLimit, Boolean isAutomatic, CouponStatus status, Boolean active) {
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.perUserLimit = perUserLimit;
        this.isAutomatic = isAutomatic;
        this.status = status;
        this.active = active;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public BigDecimal getMinPurchaseAmount() { return minPurchaseAmount; }
    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) { this.minPurchaseAmount = minPurchaseAmount; }

    public BigDecimal getMaxDiscountAmount() { return maxDiscountAmount; }
    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }

    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }

    public Integer getPerUserLimit() { return perUserLimit; }
    public void setPerUserLimit(Integer perUserLimit) { this.perUserLimit = perUserLimit; }

    public Boolean getIsAutomatic() { return isAutomatic; }
    public void setIsAutomatic(Boolean isAutomatic) { this.isAutomatic = isAutomatic; }

    public CouponStatus getStatus() { return status; }
    public void setStatus(CouponStatus status) { this.status = status; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public static CouponRequestBuilder builder() { return new CouponRequestBuilder(); }

    public static class CouponRequestBuilder {
        private String code;
        private String description;
        private DiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal minPurchaseAmount;
        private BigDecimal maxDiscountAmount;
        private Instant startDate;
        private Instant endDate;
        private Integer usageLimit;
        private Integer perUserLimit;
        private Boolean isAutomatic;
        private CouponStatus status;
        private Boolean active;

        CouponRequestBuilder() {}

        public CouponRequestBuilder code(String code) { this.code = code; return this; }
        public CouponRequestBuilder description(String description) { this.description = description; return this; }
        public CouponRequestBuilder discountType(DiscountType discountType) { this.discountType = discountType; return this; }
        public CouponRequestBuilder discountValue(BigDecimal discountValue) { this.discountValue = discountValue; return this; }
        public CouponRequestBuilder minPurchaseAmount(BigDecimal minPurchaseAmount) { this.minPurchaseAmount = minPurchaseAmount; return this; }
        public CouponRequestBuilder maxDiscountAmount(BigDecimal maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; return this; }
        public CouponRequestBuilder startDate(Instant startDate) { this.startDate = startDate; return this; }
        public CouponRequestBuilder endDate(Instant endDate) { this.endDate = endDate; return this; }
        public CouponRequestBuilder usageLimit(Integer usageLimit) { this.usageLimit = usageLimit; return this; }
        public CouponRequestBuilder perUserLimit(Integer perUserLimit) { this.perUserLimit = perUserLimit; return this; }
        public CouponRequestBuilder isAutomatic(Boolean isAutomatic) { this.isAutomatic = isAutomatic; return this; }
        public CouponRequestBuilder status(CouponStatus status) { this.status = status; return this; }
        public CouponRequestBuilder active(Boolean active) { this.active = active; return this; }

        public CouponRequest build() {
            return new CouponRequest(code, description, discountType, discountValue, minPurchaseAmount, maxDiscountAmount, startDate, endDate, usageLimit, perUserLimit, isAutomatic, status, active);
        }
    }
}
