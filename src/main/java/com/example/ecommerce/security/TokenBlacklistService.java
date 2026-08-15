package com.example.ecommerce.security;

import com.example.ecommerce.config.properties.JwtProperties;
import com.example.ecommerce.config.properties.RedisProperties;
import com.example.ecommerce.common.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;
import java.util.HexFormat;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages refresh-token sessions and blacklisted access tokens.
 *
 * <p>Normally backed by Redis via {@link StringRedisTemplate}. When {@code app.redis.enabled}
 * is {@code false} (single-instance deploys with no Redis), session bookkeeping falls back to
 * an in-memory store so authentication still works — the blacklist TTL is approximated from the
 * token's own expiry. If Redis is enabled but unreachable, operations also degrade gracefully
 * instead of failing the request.</p>
 */
@Service
@Slf4j
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;
    private final JwtService jwtService;
    private final RedisProperties redisProperties;
    private final boolean redisEnabled;

    /** In-memory fallback: hashed access token -> expiry epoch millis. */
    private final ConcurrentHashMap<String, Long> accessBlacklist = new ConcurrentHashMap<>();
    /** In-memory fallback: username -> active refresh-token session id (jti). */
    private final ConcurrentHashMap<String, String> refreshSessions = new ConcurrentHashMap<>();

    public TokenBlacklistService(StringRedisTemplate redisTemplate,
                                 JwtProperties jwtProperties,
                                 JwtService jwtService,
                                 RedisProperties redisProperties,
                                 @Value("${app.redis.enabled:false}") boolean redisEnabled) {
        this.redisTemplate = redisTemplate;
        this.jwtProperties = jwtProperties;
        this.jwtService = jwtService;
        this.redisProperties = redisProperties;
        this.redisEnabled = redisEnabled;
    }

    /**
     * Stores an access token in the blacklist until its natural expiration.
     *
     * @param token the access token to revoke
     */
    public void blacklistAccessToken(String token) {
        Date expiration = jwtService.extractExpiration(token);
        long ttlMs = Math.max(expiration.getTime() - System.currentTimeMillis(), 1_000L);
        try {
            if (redisEnabled && redisTemplate != null) {
                redisTemplate.opsForValue().set(accessBlacklistKey(token), "1", Duration.ofMillis(ttlMs));
            } else {
                accessBlacklist.put(sha256(token), expiration.getTime());
            }
        } catch (Exception ex) {
            log.warn("Redis-blacklist unavailable, using in-memory: {}", ex.getMessage());
            accessBlacklist.put(sha256(token), expiration.getTime());
        }
        log.debug("Access token blacklisted for {} ms", ttlMs);
    }

    /**
     * Checks whether an access token has been blacklisted.
     *
     * @param token the access token
     * @return {@code true} when the token is revoked
     */
    public boolean isAccessTokenBlacklisted(String token) {
        try {
            if (redisEnabled && redisTemplate != null) {
                return Boolean.TRUE.equals(redisTemplate.hasKey(accessBlacklistKey(token)));
            }
        } catch (Exception ex) {
            log.warn("Redis-blacklist check degraded to in-memory: {}", ex.getMessage());
        }
        Long expiry = accessBlacklist.get(sha256(token));
        if (expiry == null) {
            return false;
        }
        if (expiry < System.currentTimeMillis()) {
            accessBlacklist.remove(sha256(token));
            return false;
        }
        return true;
    }

    /**
     * Registers a new refresh-token session for a user.
     *
     * @param username the owning user
     * @param jti      the refresh token session id
     */
    public void storeRefreshToken(String username, String jti) {
        try {
            if (redisEnabled && redisTemplate != null) {
                redisTemplate.opsForValue().set(refreshKey(username), jti,
                        Duration.ofMillis(jwtProperties.getRefreshTokenExpirationMs()));
            } else {
                refreshSessions.put(username, jti);
            }
        } catch (Exception ex) {
            log.warn("Redis session store failed, using in-memory: {}", ex.getMessage());
            refreshSessions.put(username, jti);
        }
    }

    /**
     * Validates a presented refresh token against the active session.
     *
     * @param username the owning user
     * @param jti      the presented session id
     * @return {@code true} when the session is still active
     */
    public boolean isRefreshTokenValid(String username, String jti) {
        try {
            if (redisEnabled && redisTemplate != null) {
                String stored = redisTemplate.opsForValue().get(refreshKey(username));
                return stored != null && stored.equals(jti);
            }
        } catch (Exception ex) {
            log.warn("Redis session lookup unavailable, using in-memory: {}", ex.getMessage());
        }
        String stored = refreshSessions.get(username);
        return stored != null && stored.equals(jti);
    }

    /**
     * Revokes the active refresh session of a user.
     *
     * @param username the owning user
     */
    public void revokeRefreshToken(String username) {
        try {
            if (redisEnabled && redisTemplate != null) {
                redisTemplate.delete(refreshKey(username));
            }
        } catch (Exception ex) {
            log.warn("Redis session revoke unavailable, using in-memory: {}", ex.getMessage());
        }
        refreshSessions.remove(username);
        log.debug("Refresh token revoked for user: {}", username);
    }

    private String accessBlacklistKey(String token) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_ACCESS_TOKEN_BLACKLIST_PREFIX + sha256(token);
    }

    private String refreshKey(String username) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_REFRESH_TOKEN_PREFIX + username;
    }

    /**
     * Hashes the token so keys never expose raw credentials.
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