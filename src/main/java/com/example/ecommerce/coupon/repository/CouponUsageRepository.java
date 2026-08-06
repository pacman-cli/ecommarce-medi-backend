package com.example.ecommerce.coupon.repository;

import com.example.ecommerce.coupon.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access repository for {@link CouponUsage} audit logs.
 */
@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    long countByCouponId(Long couponId);

    long countByCouponIdAndUserId(Long couponId, Long userId);
}
