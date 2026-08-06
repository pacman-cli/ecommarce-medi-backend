package com.example.ecommerce.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Helper for generating one-time codes and one-way hashing / verification.
 *
 * <p>Codes are generated from a {@link SecureRandom} source and never persisted
 * in plain text: only their SHA-256 digest is stored, then verified using a
 * constant-time comparison to mitigate timing attacks.</p>
 */
public final class CodeGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CodeGenerator() {
    }

    /**
     * Generates a numeric one-time passcode of the given length.
     *
     * @param length the number of digits
     * @return the OTP string
     */
    public static String generateOtp(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(SECURE_RANDOM.nextInt(10));
        }
        return code.toString();
    }

    /**
     * Computes the SHA-256 hex digest of an input.
     *
     * @param value the raw value
     * @return the hex-encoded digest
     */
    public static String sha256(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm unavailable", ex);
        }
    }

    /**
     * Constant-time comparison of a raw value against a stored digest.
     *
     * @param raw         the presented plain-text value
     * @param storedDigest the stored hex digest
     * @return {@code true} when the presented value matches
     */
    public static boolean matches(String raw, String storedDigest) {
        byte[] presented = sha256(raw).getBytes(StandardCharsets.UTF_8);
        byte[] stored = storedDigest.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(presented, stored);
    }
}