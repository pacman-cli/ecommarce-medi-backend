package com.example.ecommerce.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * In-memory caching configuration for the default {@code simple} cache mode.
 *
 * <p>Commit that made {@code spring.cache.type} default to {@code simple} also made
 * {@link RedisConfig} conditional on {@code redis}, which left the application with
 * <em>no</em> {@link CacheManager} bean in the default mode — {@code CacheController}
 * and {@code RedisCacheServiceImpl} (which require a {@link CacheManager}) then fail at
 * startup with "No qualifying bean". This class restores an always-available
 * {@link ConcurrentMapCacheManager} for the {@code simple} mode; the Redis-backed
 * manager (from {@link RedisConfig}) is used when {@code spring.cache.type=redis}.</p>
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "simple", matchIfMissing = true)
public class CacheConfig {

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
