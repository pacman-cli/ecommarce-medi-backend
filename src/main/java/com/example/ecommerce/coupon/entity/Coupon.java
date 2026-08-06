package com.example.ecommerce.coupon.entity;

import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

/**
 * Enterprise coupon entity supporting percentage and fixed amount discounts,
 * minimum order thresholds, maximum discount caps, valid date windows, usage limits and automatic application.
 */
@Entity
@Table(
        name = "coupons",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_coupons_code", columnNames = "code")
        }
)
@SQLDelete(sql = "UPDATE coupons SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Coupon extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String code;

    @Column(length = 250)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType = DiscountType.PERCENTAGE;

    @Column(name = "discount_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Column(name = "min_purchase_amount", precision = 19, scale = 2)
    private BigDecimal minPurchaseAmount;

    @Column(name = "max_discount_amount", precision = 19, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    @Column(name = "per_user_limit")
    private Integer perUserLimit;

    @Column(name = "is_automatic", nullable = false)
    private boolean isAutomatic = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponStatus status = CouponStatus.ACTIVE;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Coupon() {
    }

    /**
     * Calculates discount amount for a given order subtotal.
     *
     * @param subtotal gross order subtotal
     * @return calculated monetary discount amount
     */
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal calculated = BigDecimal.ZERO;
        if (discountType == DiscountType.PERCENTAGE) {
            calculated = subtotal.multiply(discountValue).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            if (maxDiscountAmount != null && maxDiscountAmount.compareTo(BigDecimal.ZERO) > 0) {
                if (calculated.compareTo(maxDiscountAmount) > 0) {
                    calculated = maxDiscountAmount;
                }
            }
        } else if (discountType == DiscountType.FIXED_AMOUNT) {
            calculated = discountValue;
        }

        if (calculated.compareTo(subtotal) > 0) {
            calculated = subtotal;
        }

        return calculated;
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

    public Integer getUsedCount() { return usedCount; }
    public void setUsedCount(Integer usedCount) { this.usedCount = usedCount; }

    public Integer getPerUserLimit() { return perUserLimit; }
    public void setPerUserLimit(Integer perUserLimit) { this.perUserLimit = perUserLimit; }

    public boolean isAutomatic() { return isAutomatic; }
    public void setAutomatic(boolean automatic) { isAutomatic = automatic; }

    public CouponStatus getStatus() { return status; }
    public void setStatus(CouponStatus status) { this.status = status; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static CouponBuilder builder() { return new CouponBuilder(); }

    public static class CouponBuilder {
        private String code;
        private String description;
        private DiscountType discountType = DiscountType.PERCENTAGE;
        private BigDecimal discountValue = BigDecimal.ZERO;
        private BigDecimal minPurchaseAmount;
        private BigDecimal maxDiscountAmount;
        private Instant startDate;
        private Instant endDate;
        private Integer usageLimit;
        private Integer usedCount = 0;
        private Integer perUserLimit;
        private boolean isAutomatic = false;
        private CouponStatus status = CouponStatus.ACTIVE;
        private boolean active = true;
        private boolean deleted = false;
        private Instant deletedAt;

        CouponBuilder() {}

        public CouponBuilder code(String code) { this.code = code; return this; }
        public CouponBuilder description(String description) { this.description = description; return this; }
        public CouponBuilder discountType(DiscountType discountType) { this.discountType = discountType; return this; }
        public CouponBuilder discountValue(BigDecimal discountValue) { this.discountValue = discountValue; return this; }
        public CouponBuilder minPurchaseAmount(BigDecimal minPurchaseAmount) { this.minPurchaseAmount = minPurchaseAmount; return this; }
        public CouponBuilder maxDiscountAmount(BigDecimal maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; return this; }
        public CouponBuilder startDate(Instant startDate) { this.startDate = startDate; return this; }
        public CouponBuilder endDate(Instant endDate) { this.endDate = endDate; return this; }
        public CouponBuilder usageLimit(Integer usageLimit) { this.usageLimit = usageLimit; return this; }
        public CouponBuilder usedCount(Integer usedCount) { this.usedCount = usedCount; return this; }
        public CouponBuilder perUserLimit(Integer perUserLimit) { this.perUserLimit = perUserLimit; return this; }
        public CouponBuilder isAutomatic(boolean isAutomatic) { this.isAutomatic = isAutomatic; return this; }
        public CouponBuilder status(CouponStatus status) { this.status = status; return this; }
        public CouponBuilder active(boolean active) { this.active = active; return this; }
        public CouponBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public CouponBuilder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }

        public Coupon build() {
            Coupon c = new Coupon();
            c.setCode(code);
            c.setDescription(description);
            c.setDiscountType(discountType != null ? discountType : DiscountType.PERCENTAGE);
            c.setDiscountValue(discountValue != null ? discountValue : BigDecimal.ZERO);
            c.setMinPurchaseAmount(minPurchaseAmount);
            c.setMaxDiscountAmount(maxDiscountAmount);
            c.setStartDate(startDate);
            c.setEndDate(endDate);
            c.setUsageLimit(usageLimit);
            c.setUsedCount(usedCount != null ? usedCount : 0);
            c.setPerUserLimit(perUserLimit);
            c.setAutomatic(isAutomatic);
            c.setStatus(status != null ? status : CouponStatus.ACTIVE);
            c.setActive(active);
            c.setDeleted(deleted);
            c.setDeletedAt(deletedAt);
            return c;
        }
    }
}
