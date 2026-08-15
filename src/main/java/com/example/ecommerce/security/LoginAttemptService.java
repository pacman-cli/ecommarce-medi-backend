package com.example.ecommerce.security;

import com.example.ecommerce.common.constant.AppConstants;
import com.example.ecommerce.config.properties.RedisProperties;
import com.example.ecommerce.config.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Brute-force protection for the login endpoint.
 *
 * <p>Failed attempts are counted per account within a rolling window; once the
 * threshold is reached the account is locked until the lockout window elapses.
 * Normally backed by Redis; falls back to in-memory storage when {@code app.redis.enabled}
 * is {@code false} or Redis is unreachable, so login never hard-fails without Redis.</p>
 */
@Service
@Slf4j
public class LoginAttemptService {

    private final StringRedisTemplate redisTemplate;
    private final RedisProperties redisProperties;
    private final SecurityProperties securityProperties;
    private final boolean redisEnabled;

    /** In-memory fallback: username -> failed attempts. */
    private final ConcurrentHashMap<String, AtomicInteger> attempts = new ConcurrentHashMap<>();
    /** In-memory fallback: username -> lockout-expiry epoch millis. */
    private final ConcurrentHashMap<String, Long> blockedUntil = new ConcurrentHashMap<>();

    public LoginAttemptService(StringRedisTemplate redisTemplate,
                               RedisProperties redisProperties,
                               SecurityProperties securityProperties,
                               @Value("${app.redis.enabled:false}") boolean redisEnabled) {
        this.redisTemplate = redisTemplate;
        this.redisProperties = redisProperties;
        this.securityProperties = securityProperties;
        this.redisEnabled = redisEnabled;
    }

    /**
     * Checks whether the account is currently locked out.
     *
     * @param username the login identifier (email)
     * @return {@code true} when locked
     */
    public boolean isBlocked(String username) {
        try {
            if (redisEnabled && redisTemplate != null && redisTemplate.hasKey(blockedKey(username))) {
                return true;
            }
        } catch (Exception ex) {
            log.warn("Redis lockout check unavailable, using in-memory: {}", ex.getMessage());
        }
        Long until = blockedUntil.get(username);
        if (until == null) {
            return false;
        }
        if (until < System.currentTimeMillis()) {
            blockedUntil.remove(username);
            attempts.remove(username);
            return false;
        }
        return true;
    }

    /**
     * Registers a failed login and locks the account when the threshold is hit.
     *
     * @param username the login identifier (email)
     */
    public void recordFailure(String username) {
        Duration lockout = Duration.ofMinutes(securityProperties.getLockoutDurationMinutes());
        try {
            if (redisEnabled && redisTemplate != null) {
                String attemptsKey = attemptsKey(username);
                Long count = redisTemplate.opsForValue().increment(attemptsKey);
                if (count != null && count >= securityProperties.getMaxFailedLoginAttempts()) {
                    redisTemplate.opsForValue().set(blockedKey(username), "1", lockout);
                    redisTemplate.expire(attemptsKey, lockout);
                    log.warn("Account temporarily locked after failed logins: {}", username);
                } else {
                    redisTemplate.expire(attemptsKey, lockout);
                }
                return;
            }
        } catch (Exception ex) {
            log.warn("Redis failure tracking degraded, using in-memory: {}", ex.getMessage());
        }

        AtomicInteger counter = attempts.computeIfAbsent(username, k -> new AtomicInteger(0));
        if (counter.incrementAndGet() >= securityProperties.getMaxFailedLoginAttempts()) {
            blockedUntil.put(username, System.currentTimeMillis() + lockout.toMillis());
            attempts.remove(username);
            log.warn("Account temporarily locked after failed logins (in-memory): {}", username);
        }
    }

    /**
     * Clears the failure counter and lockout flag after a successful login.
     *
     * @param username the login identifier (email)
     */
    public void reset(String username) {
        try {
            if (redisEnabled && redisTemplate != null) {
                redisTemplate.delete(attemptsKey(username));
                redisTemplate.delete(blockedKey(username));
            }
        } catch (Exception ex) {
            log.warn("Redis reset unavailable: {}", ex.getMessage());
        }
        attempts.remove(username);
        blockedUntil.remove(username);
    }

    private String attemptsKey(String username) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_LOGIN_ATTEMPTS_PREFIX + username;
    }

    private String blockedKey(String username) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_LOGIN_BLOCKED_PREFIX + username;
    }
}