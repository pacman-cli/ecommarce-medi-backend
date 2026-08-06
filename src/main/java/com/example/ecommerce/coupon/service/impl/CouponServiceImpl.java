package com.example.ecommerce.coupon.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.coupon.dto.request.CouponFilterRequest;
import com.example.ecommerce.coupon.dto.request.CouponRequest;
import com.example.ecommerce.coupon.dto.request.ValidateCouponRequest;
import com.example.ecommerce.coupon.dto.response.CouponResponse;
import com.example.ecommerce.coupon.dto.response.CouponValidationResponse;
import com.example.ecommerce.coupon.entity.Coupon;
import com.example.ecommerce.coupon.entity.CouponStatus;
import com.example.ecommerce.coupon.entity.CouponUsage;
import com.example.ecommerce.coupon.mapper.CouponMapper;
import com.example.ecommerce.coupon.repository.CouponRepository;
import com.example.ecommerce.coupon.repository.CouponUsageRepository;
import com.example.ecommerce.coupon.service.CouponService;
import com.example.ecommerce.coupon.specification.CouponSpecification;
import com.example.ecommerce.coupon.validator.CouponValidator;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing promotional coupons, running validation rules,
 * finding automatic coupons and logging redemption usage.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final UserRepository userRepository;
    private final CouponMapper couponMapper;
    private final CouponValidator couponValidator;

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        log.info("Creating coupon with code: {}", request.getCode());
        couponValidator.validateCouponRequest(request, null);

        Coupon coupon = couponMapper.toEntity(request);
        coupon.setCode(request.getCode().trim().toUpperCase());
        if (request.getIsAutomatic() != null) {
            coupon.setAutomatic(request.getIsAutomatic());
        }

        Coupon saved = couponRepository.save(coupon);
        log.info("Successfully created coupon with ID: {}", saved.getId());
        return couponMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        log.info("Updating coupon ID: {}", id);
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + id));

        couponValidator.validateCouponRequest(request, id);

        couponMapper.updateEntityFromRequest(request, coupon);
        coupon.setCode(request.getCode().trim().toUpperCase());
        if (request.getIsAutomatic() != null) {
            coupon.setAutomatic(request.getIsAutomatic());
        }

        Coupon updated = couponRepository.save(coupon);
        log.info("Successfully updated coupon ID: {}", updated.getId());
        return couponMapper.toResponse(updated);
    }

    @Override
    public CouponResponse getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + id));
        return couponMapper.toResponse(coupon);
    }

    @Override
    public CouponResponse getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCodeIgnoreCaseAndDeletedFalse(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code));
        return couponMapper.toResponse(coupon);
    }

    @Override
    public PageResponse<CouponResponse> getCoupons(CouponFilterRequest filter, Pageable pageable) {
        Specification<Coupon> spec = CouponSpecification.build(filter);
        Page<Coupon> page = couponRepository.findAll(spec, pageable);
        return PageResponse.from(page, couponMapper::toResponse);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        log.info("Soft deleting coupon ID: {}", id);
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + id));

        coupon.setDeleted(true);
        coupon.setDeletedAt(Instant.now());
        coupon.setStatus(CouponStatus.INACTIVE);
        coupon.setActive(false);
        couponRepository.save(coupon);
        log.info("Successfully soft deleted coupon ID: {}", id);
    }

    @Override
    public CouponValidationResponse validateCoupon(ValidateCouponRequest request) {
        log.info("Evaluating coupon validation for code '{}'", request.getCouponCode());
        Optional<Coupon> couponOpt = couponRepository.findByCodeIgnoreCaseAndDeletedFalse(request.getCouponCode());
        if (couponOpt.isEmpty()) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .couponCode(request.getCouponCode())
                    .discountAmount(BigDecimal.ZERO)
                    .message("Coupon code '" + request.getCouponCode() + "' does not exist")
                    .build();
        }

        User currentUser = getCurrentUserEntity();
        Long userId = currentUser != null ? currentUser.getId() : null;

        return couponValidator.evaluateCouponValidation(couponOpt.get(), request.getSubtotal(), userId);
    }

    @Override
    public List<CouponResponse> getApplicableAutomaticCoupons(BigDecimal subtotal) {
        List<Coupon> autoCoupons = couponRepository.findByIsAutomaticTrueAndStatusAndActiveTrueAndDeletedFalse(CouponStatus.ACTIVE);
        User currentUser = getCurrentUserEntity();
        Long userId = currentUser != null ? currentUser.getId() : null;

        List<Coupon> validCoupons = autoCoupons.stream()
                .filter(c -> couponValidator.evaluateCouponValidation(c, subtotal, userId).isValid())
                .toList();

        return couponMapper.toResponseList(validCoupons);
    }

    @Override
    @Transactional
    public void recordCouponUsage(Long couponId, Long userId, Long orderId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + couponId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        CouponUsage usage = CouponUsage.builder()
                .coupon(coupon)
                .user(user)
                .orderId(orderId)
                .usedAt(Instant.now())
                .build();
        couponUsageRepository.save(usage);
        log.info("Recorded coupon usage for coupon ID {}, user ID {}, order ID {}", couponId, userId, orderId);
    }

    private User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            return userRepository.findByEmailIgnoreCase(email).orElse(null);
        }
        return null;
    }
}
