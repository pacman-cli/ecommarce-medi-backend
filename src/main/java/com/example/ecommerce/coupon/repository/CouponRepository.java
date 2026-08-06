package com.example.ecommerce.coupon.repository;

import com.example.ecommerce.coupon.entity.Coupon;
import com.example.ecommerce.coupon.entity.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Coupon} entities.
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    Optional<Coupon> findByCodeIgnoreCaseAndDeletedFalse(String code);

    boolean existsByCodeIgnoreCaseAndDeletedFalse(String code);

    boolean existsByCodeIgnoreCaseAndDeletedFalseAndIdNot(String code, Long id);

    List<Coupon> findByIsAutomaticTrueAndStatusAndActiveTrueAndDeletedFalse(CouponStatus status);
}
