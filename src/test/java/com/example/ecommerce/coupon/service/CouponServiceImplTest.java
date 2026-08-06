package com.example.ecommerce.coupon.service;

import com.example.ecommerce.coupon.dto.request.CouponRequest;
import com.example.ecommerce.coupon.dto.request.ValidateCouponRequest;
import com.example.ecommerce.coupon.dto.response.CouponResponse;
import com.example.ecommerce.coupon.dto.response.CouponValidationResponse;
import com.example.ecommerce.coupon.entity.Coupon;
import com.example.ecommerce.coupon.entity.CouponStatus;
import com.example.ecommerce.coupon.entity.DiscountType;
import com.example.ecommerce.coupon.mapper.CouponMapper;
import com.example.ecommerce.coupon.repository.CouponRepository;
import com.example.ecommerce.coupon.repository.CouponUsageRepository;
import com.example.ecommerce.coupon.service.impl.CouponServiceImpl;
import com.example.ecommerce.coupon.validator.CouponValidator;
import com.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponUsageRepository couponUsageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private CouponValidator couponValidator;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Coupon coupon;
    private CouponRequest couponRequest;
    private CouponResponse couponResponse;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .code("SAVE20")
                .description("20% off on medicine")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(new BigDecimal("20.00"))
                .minPurchaseAmount(new BigDecimal("50.00"))
                .maxDiscountAmount(new BigDecimal("15.00"))
                .status(CouponStatus.ACTIVE)
                .active(true)
                .build();
        coupon.setId(1L);

        couponRequest = CouponRequest.builder()
                .code("SAVE20")
                .description("20% off on medicine")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(new BigDecimal("20.00"))
                .minPurchaseAmount(new BigDecimal("50.00"))
                .maxDiscountAmount(new BigDecimal("15.00"))
                .status(CouponStatus.ACTIVE)
                .active(true)
                .build();

        couponResponse = CouponResponse.builder()
                .id(1L)
                .code("SAVE20")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(new BigDecimal("20.00"))
                .status(CouponStatus.ACTIVE)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("createCoupon should validate, map to entity, save and return response DTO")
    void createCoupon_Success() {
        doNothing().when(couponValidator).validateCouponRequest(couponRequest, null);
        when(couponMapper.toEntity(couponRequest)).thenReturn(coupon);
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);
        when(couponMapper.toResponse(any(Coupon.class))).thenReturn(couponResponse);

        CouponResponse response = couponService.createCoupon(couponRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("SAVE20");

        verify(couponValidator).validateCouponRequest(couponRequest, null);
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("validateCoupon should evaluate validation engine and return outcome")
    void validateCoupon_Success() {
        ValidateCouponRequest valReq = ValidateCouponRequest.builder()
                .couponCode("SAVE20")
                .subtotal(new BigDecimal("100.00"))
                .build();

        CouponValidationResponse valOutcome = CouponValidationResponse.builder()
                .valid(true)
                .couponCode("SAVE20")
                .discountAmount(new BigDecimal("15.00"))
                .message("Coupon applied successfully")
                .build();

        when(couponRepository.findByCodeIgnoreCaseAndDeletedFalse("SAVE20")).thenReturn(Optional.of(coupon));
        when(couponValidator.evaluateCouponValidation(eq(coupon), eq(new BigDecimal("100.00")), any()))
                .thenReturn(valOutcome);

        CouponValidationResponse response = couponService.validateCoupon(valReq);

        assertThat(response).isNotNull();
        assertThat(response.isValid()).isTrue();
        assertThat(response.getDiscountAmount()).isEqualTo(new BigDecimal("15.00"));
    }

    @Test
    @DisplayName("deleteCoupon should soft delete coupon and set status INACTIVE")
    void deleteCoupon_Success() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        couponService.deleteCoupon(1L);

        assertThat(coupon.isDeleted()).isTrue();
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.INACTIVE);
        assertThat(coupon.isActive()).isFalse();

        verify(couponRepository).save(coupon);
    }
}
