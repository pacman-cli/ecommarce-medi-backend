package com.example.ecommerce.coupon.dto.response;

import com.example.ecommerce.coupon.entity.CouponStatus;
import com.example.ecommerce.coupon.entity.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Detailed coupon projection response DTO.
 */
@Schema(description = "Coupon details response")
public class CouponResponse {

    @Schema(description = "Coupon ID", example = "1")
    private Long id;

    @Schema(description = "Unique uppercase code", example = "SAVE20")
    private String code;

    @Schema(description = "Coupon description", example = "20% off on all pharmaceutical orders")
    private String description;

    @Schema(description = "Discount calculation type", example = "PERCENTAGE")
    private DiscountType discountType;

    @Schema(description = "Discount monetary value or percentage", example = "20.00")
    private BigDecimal discountValue;

    @Schema(description = "Minimum subtotal required to redeem", example = "50.00")
    private BigDecimal minPurchaseAmount;

    @Schema(description = "Maximum monetary cap for percentage discount", example = "15.00")
    private BigDecimal maxDiscountAmount;

    @Schema(description = "Activation start timestamp", example = "2026-08-01T00:00:00Z")
    private Instant startDate;

    @Schema(description = "Expiration end timestamp", example = "2026-12-31T23:59:59Z")
    private Instant endDate;

    @Schema(description = "Global usage limit across all users", example = "1000")
    private Integer usageLimit;

    @Schema(description = "Current global redemptions count", example = "142")
    private Integer usedCount;

    @Schema(description = "Redemptions limit per user", example = "1")
    private Integer perUserLimit;

    @Schema(description = "Automatic application flag", example = "false")
    private boolean isAutomatic;

    @Schema(description = "Operational status", example = "ACTIVE")
    private CouponStatus status;

    @Schema(description = "Active flag", example = "true")
    private boolean active;

    @Schema(description = "Created timestamp")
    private Instant createdAt;

    @Schema(description = "Updated timestamp")
    private Instant updatedAt;

    public CouponResponse() {
    }

    public CouponResponse(Long id, String code, String description, DiscountType discountType, BigDecimal discountValue, BigDecimal minPurchaseAmount, BigDecimal maxDiscountAmount, Instant startDate, Instant endDate, Integer usageLimit, Integer usedCount, Integer perUserLimit, boolean isAutomatic, CouponStatus status, boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
        this.perUserLimit = perUserLimit;
        this.isAutomatic = isAutomatic;
        this.status = status;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Integer getUsedCount() { return usedCount; }
    public void setUsedCount(Integer usedCount) { this.usedCount = usedCount; }

    public Integer getPerUserLimit() { return perUserLimit; }
    public void setPerUserLimit(Integer perUserLimit) { this.perUserLimit = perUserLimit; }

    public boolean getIsAutomatic() { return isAutomatic; }
    public void setIsAutomatic(boolean automatic) { isAutomatic = automatic; }

    public CouponStatus getStatus() { return status; }
    public void setStatus(CouponStatus status) { this.status = status; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static CouponResponseBuilder builder() { return new CouponResponseBuilder(); }

    public static class CouponResponseBuilder {
        private Long id;
        private String code;
        private String description;
        private DiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal minPurchaseAmount;
        private BigDecimal maxDiscountAmount;
        private Instant startDate;
        private Instant endDate;
        private Integer usageLimit;
        private Integer usedCount;
        private Integer perUserLimit;
        private boolean isAutomatic;
        private CouponStatus status;
        private boolean active;
        private Instant createdAt;
        private Instant updatedAt;

        CouponResponseBuilder() {}

        public CouponResponseBuilder id(Long id) { this.id = id; return this; }
        public CouponResponseBuilder code(String code) { this.code = code; return this; }
        public CouponResponseBuilder description(String description) { this.description = description; return this; }
        public CouponResponseBuilder discountType(DiscountType discountType) { this.discountType = discountType; return this; }
        public CouponResponseBuilder discountValue(BigDecimal discountValue) { this.discountValue = discountValue; return this; }
        public CouponResponseBuilder minPurchaseAmount(BigDecimal minPurchaseAmount) { this.minPurchaseAmount = minPurchaseAmount; return this; }
        public CouponResponseBuilder maxDiscountAmount(BigDecimal maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; return this; }
        public CouponResponseBuilder startDate(Instant startDate) { this.startDate = startDate; return this; }
        public CouponResponseBuilder endDate(Instant endDate) { this.endDate = endDate; return this; }
        public CouponResponseBuilder usageLimit(Integer usageLimit) { this.usageLimit = usageLimit; return this; }
        public CouponResponseBuilder usedCount(Integer usedCount) { this.usedCount = usedCount; return this; }
        public CouponResponseBuilder perUserLimit(Integer perUserLimit) { this.perUserLimit = perUserLimit; return this; }
        public CouponResponseBuilder isAutomatic(boolean isAutomatic) { this.isAutomatic = isAutomatic; return this; }
        public CouponResponseBuilder status(CouponStatus status) { this.status = status; return this; }
        public CouponResponseBuilder active(boolean active) { this.active = active; return this; }
        public CouponResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public CouponResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public CouponResponse build() {
            return new CouponResponse(id, code, description, discountType, discountValue, minPurchaseAmount, maxDiscountAmount, startDate, endDate, usageLimit, usedCount, perUserLimit, isAutomatic, status, active, createdAt, updatedAt);
        }
    }
}
