package com.example.ecommerce.coupon.validator;

import com.example.ecommerce.coupon.dto.request.CouponRequest;
import com.example.ecommerce.coupon.dto.response.CouponValidationResponse;
import com.example.ecommerce.coupon.entity.Coupon;
import com.example.ecommerce.coupon.entity.CouponStatus;
import com.example.ecommerce.coupon.entity.DiscountType;
import com.example.ecommerce.coupon.repository.CouponRepository;
import com.example.ecommerce.coupon.repository.CouponUsageRepository;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Enterprise validation engine evaluating coupon creation rules and checkout redemption validity.
 */
@Component
@RequiredArgsConstructor
public class CouponValidator {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;

    /**
     * Validates business constraints during coupon creation or update.
     */
    public void validateCouponRequest(CouponRequest request, Long existingId) {
        String code = request.getCode().trim().toUpperCase();

        if (existingId == null) {
            if (couponRepository.existsByCodeIgnoreCaseAndDeletedFalse(code)) {
                throw new ConflictException("Coupon code '" + code + "' already exists");
            }
        } else {
            if (couponRepository.existsByCodeIgnoreCaseAndDeletedFalseAndIdNot(code, existingId)) {
                throw new ConflictException("Coupon code '" + code + "' is taken by another coupon");
            }
        }

        if (request.getDiscountType() == DiscountType.PERCENTAGE) {
            if (request.getDiscountValue().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new BadRequestException("Percentage discount value cannot exceed 100%");
            }
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new BadRequestException("Coupon start date cannot be after end date");
            }
        }
    }

    /**
     * Coupon validation engine evaluating eligibility and calculated discount for a cart.
     */
    public CouponValidationResponse evaluateCouponValidation(Coupon coupon, BigDecimal subtotal, Long userId) {
        if (coupon == null || coupon.isDeleted()) {
            return invalidResponse("Invalid coupon code", null);
        }

        String code = coupon.getCode();

        if (!coupon.isActive() || coupon.getStatus() != CouponStatus.ACTIVE) {
            return invalidResponse("Coupon '" + code + "' is inactive", code);
        }

        Instant now = Instant.now();
        if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) {
            return invalidResponse("Coupon '" + code + "' is not active yet", code);
        }
        if (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate())) {
            return invalidResponse("Coupon '" + code + "' has expired", code);
        }

        if (coupon.getMinPurchaseAmount() != null && subtotal.compareTo(coupon.getMinPurchaseAmount()) < 0) {
            return invalidResponse("Minimum purchase amount of $" + coupon.getMinPurchaseAmount() + " required to use coupon '" + code + "'", code);
        }

        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            return invalidResponse("Coupon '" + code + "' has reached its total global usage limit", code);
        }

        if (userId != null && coupon.getPerUserLimit() != null) {
            long userUsages = couponUsageRepository.countByCouponIdAndUserId(coupon.getId(), userId);
            if (userUsages >= coupon.getPerUserLimit()) {
                return invalidResponse("You have reached your maximum usage limit (" + coupon.getPerUserLimit() + ") for coupon '" + code + "'", code);
            }
        }

        BigDecimal discount = coupon.calculateDiscount(subtotal);
        return CouponValidationResponse.builder()
                .valid(true)
                .couponCode(code)
                .discountAmount(discount)
                .message("Coupon '" + code + "' applied successfully ($" + discount + " discount)")
                .build();
    }

    private CouponValidationResponse invalidResponse(String message, String code) {
        return CouponValidationResponse.builder()
                .valid(false)
                .couponCode(code)
                .discountAmount(BigDecimal.ZERO)
                .message(message)
                .build();
    }
}
