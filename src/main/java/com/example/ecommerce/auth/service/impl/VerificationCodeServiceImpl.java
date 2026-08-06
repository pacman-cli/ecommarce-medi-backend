package com.example.ecommerce.auth.service.impl;

import com.example.ecommerce.auth.entity.VerificationCode;
import com.example.ecommerce.auth.entity.VerificationCodeType;
import com.example.ecommerce.auth.repository.VerificationCodeRepository;
import com.example.ecommerce.auth.service.VerificationCodeService;
import com.example.ecommerce.common.util.CodeGenerator;
import com.example.ecommerce.config.properties.VerificationProperties;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

/**
 * Implementation of {@link VerificationCodeService}.
 *
 * <p>Each issuance rotates the code: previous unused codes of the same type are
 * immediately invalidated so only the newest code can be redeemed.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationProperties verificationProperties;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String issueCode(User user, VerificationCodeType type) {
        invalidateActive(user.getId(), type);
        String plainCode = CodeGenerator.generateOtp(verificationProperties.getOtpLength());
        VerificationCode code = VerificationCode.builder()
                .user(user)
                .type(type)
                .codeHash(CodeGenerator.sha256(plainCode))
                .expiresAt(Instant.now().plus(Duration.ofMinutes(verificationProperties.getOtpTtlMinutes())))
                .build();
        verificationCodeRepository.save(code);
        log.info("Issued {} code for user id={}, expires in {} minutes",
                type, user.getId(), verificationProperties.getOtpTtlMinutes());
        return plainCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void verify(Long userId, VerificationCodeType type, String code) {
        VerificationCode persisted = verificationCodeRepository
                .findTopByUserIdAndTypeAndUsedAtIsNullOrderByCreatedAtDesc(userId, type)
                .orElseThrow(() -> new BadRequestException("No active verification code found. Please request a new one."));

        if (persisted.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Verification code has expired. Please request a new one.");
        }
        if (persisted.getAttempts() >= verificationProperties.getMaxAttempts()) {
            invalidateActive(userId, type);
            throw new BadRequestException("Too many failed attempts. Please request a new code.");
        }
        if (!CodeGenerator.matches(code, persisted.getCodeHash())) {
            persisted.setAttempts(persisted.getAttempts() + 1);
            verificationCodeRepository.save(persisted);
            log.warn("Invalid verification code presented for user id={} (attempt {})",
                    userId, persisted.getAttempts());
            throw new BadRequestException("Incorrect verification code.");
        }

        persisted.setUsedAt(Instant.now());
        verificationCodeRepository.save(persisted);
        log.info("Verified {} code for user id={}", type, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void invalidateActive(Long userId, VerificationCodeType type) {
        int invalidated = verificationCodeRepository.markAllUsedFor(userId, type, Instant.now());
        if (invalidated > 0) {
            log.debug("Invalidated {} unused {} codes for user id={}", invalidated, type, userId);
        }
    }
}
