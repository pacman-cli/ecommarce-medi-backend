package com.example.ecommerce.cache.service.impl;

import com.example.ecommerce.cache.constant.CacheNames;
import com.example.ecommerce.cache.dto.response.CacheRegionInfoResponse;
import com.example.ecommerce.cache.service.RedisCacheService;
import com.example.ecommerce.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link RedisCacheService} managing programmatic Redis cache inspections,
 * targeted region evictions, and full cache flushes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements RedisCacheService {

    private final CacheManager cacheManager;

    @Override
    public List<CacheRegionInfoResponse> getCacheNames() {
        Collection<String> names = cacheManager.getCacheNames();
        List<CacheRegionInfoResponse> list = new ArrayList<>();

        for (String name : names) {
            String ttlDesc = resolveTtlDescription(name);
            list.add(CacheRegionInfoResponse.builder()
                    .name(name)
                    .configuredTtl(ttlDesc)
                    .build());
        }

        return list;
    }

    @Override
    public void evictCache(String cacheName) {
        log.info("Programmatically evicting all entries in cache region: {}", cacheName);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new ResourceNotFoundException("Cache region", "name", cacheName);
        }
        cache.clear();
        log.info("Successfully evicted cache region: {}", cacheName);
    }

    @Override
    public void evictCacheKey(String cacheName, Object key) {
        log.info("Evicting key '{}' in cache region: {}", key, cacheName);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new ResourceNotFoundException("Cache region", "name", cacheName);
        }
        cache.evict(key);
    }

    @Override
    public void clearAllCaches() {
        log.info("Clearing all application Redis cache regions");
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        }
        log.info("Successfully cleared all application cache regions");
    }

    private String resolveTtlDescription(String cacheName) {
        return switch (cacheName) {
            case CacheNames.PRODUCTS, CacheNames.PRODUCT_DETAILS -> "1 Hour";
            case CacheNames.CATEGORIES, CacheNames.CATEGORY_TREE, CacheNames.BRANDS, CacheNames.ACTIVE_BRANDS -> "24 Hours";
            case CacheNames.SEARCH_AUTOCOMPLETE -> "30 Minutes";
            case CacheNames.SEARCH_TRENDING -> "15 Minutes";
            case CacheNames.DASHBOARD_SUMMARY -> "5 Minutes";
            default -> "10 Minutes (Default)";
        };
    }
}
