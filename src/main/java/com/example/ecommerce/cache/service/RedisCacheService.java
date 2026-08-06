package com.example.ecommerce.cache.service;

import com.example.ecommerce.cache.dto.response.CacheRegionInfoResponse;

import java.util.List;

/**
 * Service interface defining programmatic operations for inspecting, evicting,
 * and flushing Redis application cache regions.
 */
public interface RedisCacheService {

    /**
     * Retrieves all active application cache region names and TTL policies.
     */
    List<CacheRegionInfoResponse> getCacheNames();

    /**
     * Evicts all entries within specified cache region name.
     */
    void evictCache(String cacheName);

    /**
     * Evicts a specific key entry within a cache region.
     */
    void evictCacheKey(String cacheName, Object key);

    /**
     * Flushes and clears all application cache regions.
     */
    void clearAllCaches();
}
