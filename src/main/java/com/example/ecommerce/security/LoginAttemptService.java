package com.example.ecommerce.security;

import com.example.ecommerce.common.constant.AppConstants;
import com.example.ecommerce.config.properties.RedisProperties;
import com.example.ecommerce.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Redis-backed brute-force protection for the login endpoint.
 *
 * <p>Failed attempts are counted per account within a rolling window; once the
 * threshold is reached the account is locked until the lockout window elapses.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAttemptService {

    private final StringRedisTemplate redisTemplate;
    private final RedisProperties redisProperties;
    private final SecurityProperties securityProperties;

    /**
     * Checks whether the account is currently locked out.
     *
     * @param username the login identifier (email)
     * @return {@code true} when locked
     */
    public boolean isBlocked(String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(blockedKey(username)));
    }

    /**
     * Registers a failed login and locks the account when the threshold is hit.
     *
     * @param username the login identifier (email)
     */
    public void recordFailure(String username) {
        String attemptsKey = attemptsKey(username);
        Duration lockout = Duration.ofMinutes(securityProperties.getLockoutDurationMinutes());
        Long count = redisTemplate.opsForValue().increment(attemptsKey);
        if (count != null && count >= securityProperties.getMaxFailedLoginAttempts()) {
            redisTemplate.opsForValue().set(blockedKey(username), "1", lockout);
            redisTemplate.expire(attemptsKey, lockout);
            log.warn("Account temporarily locked after failed logins: {}", username);
        } else {
            redisTemplate.expire(attemptsKey, lockout);
        }
    }

    /**
     * Clears the failure counter and lockout flag after a successful login.
     *
     * @param username the login identifier (email)
     */
    public void reset(String username) {
        redisTemplate.delete(attemptsKey(username));
        redisTemplate.delete(blockedKey(username));
    }

    private String attemptsKey(String username) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_LOGIN_ATTEMPTS_PREFIX + username;
    }

    private String blockedKey(String username) {
        return redisProperties.getKeyPrefix() + AppConstants.REDIS_LOGIN_BLOCKED_PREFIX + username;
    }
}
