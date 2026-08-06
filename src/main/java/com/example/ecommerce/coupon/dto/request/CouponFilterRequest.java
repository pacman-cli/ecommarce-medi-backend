package com.example.ecommerce.coupon.dto.request;

import com.example.ecommerce.coupon.entity.CouponStatus;
import com.example.ecommerce.coupon.entity.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Filter parameters for searching and listing promotional coupons.
 */
@Schema(description = "Coupon search and filter criteria")
public class CouponFilterRequest {

    @Schema(description = "Keyword search matching code or description", example = "SAVE")
    private String search;

    @Schema(description = "Filter by discount type", example = "PERCENTAGE")
    private DiscountType discountType;

    @Schema(description = "Filter by coupon status", example = "ACTIVE")
    private CouponStatus status;

    @Schema(description = "Filter automatic vs manual coupons", example = "false")
    private Boolean isAutomatic;

    @Schema(description = "Filter active non-expired coupons only", example = "true")
    private Boolean activeOnly;

    public CouponFilterRequest() {
    }

    public CouponFilterRequest(String search, DiscountType discountType, CouponStatus status, Boolean isAutomatic, Boolean activeOnly) {
        this.search = search;
        this.discountType = discountType;
        this.status = status;
        this.isAutomatic = isAutomatic;
        this.activeOnly = activeOnly;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }

    public CouponStatus getStatus() { return status; }
    public void setStatus(CouponStatus status) { this.status = status; }

    public Boolean getIsAutomatic() { return isAutomatic; }
    public void setIsAutomatic(Boolean isAutomatic) { this.isAutomatic = isAutomatic; }

    public Boolean getActiveOnly() { return activeOnly; }
    public void setActiveOnly(Boolean activeOnly) { this.activeOnly = activeOnly; }

    public static CouponFilterRequestBuilder builder() { return new CouponFilterRequestBuilder(); }

    public static class CouponFilterRequestBuilder {
        private String search;
        private DiscountType discountType;
        private CouponStatus status;
        private Boolean isAutomatic;
        private Boolean activeOnly;

        CouponFilterRequestBuilder() {}

        public CouponFilterRequestBuilder search(String search) { this.search = search; return this; }
        public CouponFilterRequestBuilder discountType(DiscountType discountType) { this.discountType = discountType; return this; }
        public CouponFilterRequestBuilder status(CouponStatus status) { this.status = status; return this; }
        public CouponFilterRequestBuilder isAutomatic(Boolean isAutomatic) { this.isAutomatic = isAutomatic; return this; }
        public CouponFilterRequestBuilder activeOnly(Boolean activeOnly) { this.activeOnly = activeOnly; return this; }

        public CouponFilterRequest build() {
            return new CouponFilterRequest(search, discountType, status, isAutomatic, activeOnly);
        }
    }
}
