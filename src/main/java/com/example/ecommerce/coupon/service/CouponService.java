package com.example.ecommerce.coupon.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.coupon.dto.request.CouponFilterRequest;
import com.example.ecommerce.coupon.dto.request.CouponRequest;
import com.example.ecommerce.coupon.dto.request.ValidateCouponRequest;
import com.example.ecommerce.coupon.dto.response.CouponResponse;
import com.example.ecommerce.coupon.dto.response.CouponValidationResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for managing promotional coupons, validation engine evaluations,
 * automatic coupon matching and redemption tracking.
 */
public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    CouponResponse updateCoupon(Long id, CouponRequest request);

    CouponResponse getCouponById(Long id);

    CouponResponse getCouponByCode(String code);

    PageResponse<CouponResponse> getCoupons(CouponFilterRequest filter, Pageable pageable);

    void deleteCoupon(Long id);

    CouponValidationResponse validateCoupon(ValidateCouponRequest request);

    List<CouponResponse> getApplicableAutomaticCoupons(BigDecimal subtotal);

    void recordCouponUsage(Long couponId, Long userId, Long orderId);
}
