package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.entity.VerificationCodeType;
import com.example.ecommerce.user.entity.User;

/**
 * Contract for the one-time verification code lifecycle.
 */
public interface VerificationCodeService {

    /**
     * Issues a new code for a user and purpose, invalidating any previous
     * unused codes of the same type.
     *
     * @param user the owning user
     * @param type the code purpose
     * @return the plain-text code (to be delivered out-of-band, never stored)
     */
    String issueCode(User user, VerificationCodeType type);

    /**
     * Validates a presented code for a user and purpose, consuming it on success.
     *
     * @param userId the owning user id
     * @param type   the code purpose
     * @param code   the presented plain-text code
     */
    void verify(Long userId, VerificationCodeType type, String code);

    /**
     * Invalidates every unused code of a user and purpose (e.g. after a
     * successful password change).
     *
     * @param userId the owning user id
     * @param type   the code purpose
     */
    void invalidateActive(Long userId, VerificationCodeType type);
}