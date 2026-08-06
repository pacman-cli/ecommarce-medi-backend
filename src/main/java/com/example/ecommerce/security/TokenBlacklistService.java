package com.example.ecommerce.security;

import com.example.ecommerce.config.properties.JwtProperties;
import com.example.ecommerce.config.properties.RedisProperties;
import com.example.ecommerce.common.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;
import java.util.HexFormat;

/**
 * Redis-backed token store managing:
 * <ul>
 *     <li>Refresh-token sessions (one active session per user).</li>
 *     <li>Blacklisted access tokens that are no longer accepted until they expire.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;
    private final JwtService jwtService;
    private final RedisProperties redisProperties;

    /**
     * Stores an access token in the blacklist until its natural expiration.
     *
     * @param token the access token to revoke
     */
    public void blacklistAccessToken(String token) {
        Date expiration = jwtService.extractExpiration(token);
        long ttlMs = Math.max(expiration.getTime() - System.currentTimeMillis(), 1_000L);
        redisTemplate.opsForValue().set(accessBlacklistKey(token), "1", Duration.ofMillis(ttlMs));
        log.debug("Access token blacklisted for {} ms", ttlMs);
    }

    /**
     * Checks whether an access token has been blacklisted.
     *
     * @param token the access token
     * @return {@code true} when the token is revoked
     */
    public boolean isAccessTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(accessBlacklistKey(token)));
    }

    /**
     * Registers a new refresh-token session for a user.
     *
     * @param username the owning user
     * @param jti      the refresh token session id
     */
    public void storeRefreshToken(String username, String jti) {
        redisTemplate.opsForValue().set(refreshKey(username), jti,
                Duration.ofMillis(jwtProperties.getRefreshTokenExpirationMs()));
    }

    /**
     * Validates a presented refresh token against the active session.
     *
     * @param username the owning user
     * @param jti      the presented session id
     * @return {@code true} when the session is still active
     */
    public boolean isRefreshTokenValid(String username, String jti) {
        String stored = redisTemplate.opsForValue().get(refreshKey(username));
        return stored != null && stored.equals(jti);
    }

    /**
     * Revokes the active refresh session of a user.
     *
     * @param username the owning user
     */
    public void revokeRefreshToken(String username) {
        redisTemplate.delete(refreshKey(username));
        log.debug("Refresh token revoked for user: {}", username);
    }

    private String accessBlacklistKey(String token) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_ACCESS_TOKEN_BLACKLIST_PREFIX + sha256(token);
    }

    private String refreshKey(String username) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_REFRESH_TOKEN_PREFIX + username;
    }

    /**
     * Hashes the token so Redis keys never expose raw credentials.
     */
    private String sha256(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm unavailable", ex);
        }
    }
}
