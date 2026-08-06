package com.example.ecommerce.scheduler.job;

import com.example.ecommerce.coupon.entity.Coupon;
import com.example.ecommerce.coupon.entity.CouponStatus;
import com.example.ecommerce.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Background scheduled cron job deactivating expired promo coupons.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredCouponScheduler {

    private final CouponRepository couponRepository;

    @Scheduled(cron = "0 30 0 * * ?") // Daily at 00:30 AM
    @Transactional
    public void runExpiredCouponCheck() {
        log.info("[CRON JOB] Starting expired coupon deactivation check...");
        Instant now = Instant.now();
        int count = 0;

        for (Coupon coupon : couponRepository.findAll()) {
            if (!coupon.isDeleted() && coupon.getStatus() == CouponStatus.ACTIVE && coupon.getEndDate() != null && coupon.getEndDate().isBefore(now)) {
                coupon.setStatus(CouponStatus.EXPIRED);
                coupon.setActive(false);
                couponRepository.save(coupon);
                count++;
                log.info("[COUPON EXPIRED] Deactivated promo coupon code: '{}' ID: {}", coupon.getCode(), coupon.getId());
            }
        }

        log.info("[CRON JOB] Expired coupon check completed. Total coupons deactivated: {}", count);
    }
}
