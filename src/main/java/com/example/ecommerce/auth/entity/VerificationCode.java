package com.example.ecommerce.auth.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * A single-use verification code issued to a user.
 *
 * <p>Only the SHA-256 digest of the code is stored; the plain text is delivered
 * to the user's inbox and never persisted.</p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_codes", indexes = {
        @Index(name = "idx_verification_codes_user_type", columnList = "user_id,type"),
        @Index(name = "idx_verification_codes_user_type_used", columnList = "user_id,type,used_at")
})
public class VerificationCode extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VerificationCodeType type;

    /** SHA-256 hex digest of the plain-text code. */
    @Column(name = "code_hash", nullable = false, length = 64)
    private String codeHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /** Set when the code has been successfully redeemed. */
    @Column(name = "used_at")
    private Instant usedAt;

    /** Number of failed verification attempts against this code. */
    @Column(nullable = false)
    @Builder.Default
    private int attempts = 0;
}