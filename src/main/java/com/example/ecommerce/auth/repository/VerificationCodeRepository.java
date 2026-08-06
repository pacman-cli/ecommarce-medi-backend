package com.example.ecommerce.auth.repository;

import com.example.ecommerce.auth.entity.VerificationCode;
import com.example.ecommerce.auth.entity.VerificationCodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

/**
 * Data access for {@link VerificationCode} aggregates.
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    /**
     * Returns the most recently issued unused code for a user and purpose.
     *
     * @param userId the owning user id
     * @param type   the code purpose
     * @return the latest unused code, if any
     */
    Optional<VerificationCode> findTopByUserIdAndTypeAndUsedAtIsNullOrderByCreatedAtDesc(Long userId, VerificationCodeType type);

    /**
     * Marks every unused code of a user and purpose as used, invalidating them.
     *
     * @param userId the owning user id
     * @param type   the code purpose
     * @param now    the current instant used as the consumption timestamp
     * @return the number of invalidated codes
     */
    @Modifying
    @Query("UPDATE VerificationCode c SET c.usedAt = :now WHERE c.user.id = :userId AND c.type = :type AND c.usedAt IS NULL")
    int markAllUsedFor(@Param("userId") Long userId, @Param("type") VerificationCodeType type, @Param("now") Instant now);
}