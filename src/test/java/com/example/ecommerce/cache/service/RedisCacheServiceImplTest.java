package com.example.ecommerce.cache.service;

import com.example.ecommerce.cache.constant.CacheNames;
import com.example.ecommerce.cache.dto.response.CacheRegionInfoResponse;
import com.example.ecommerce.cache.service.impl.RedisCacheServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisCacheServiceImplTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache sampleCache;

    @InjectMocks
    private RedisCacheServiceImpl redisCacheService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetCacheNamesSuccess() {
        when(cacheManager.getCacheNames()).thenReturn(Arrays.asList(CacheNames.PRODUCTS, CacheNames.CATEGORIES));

        List<CacheRegionInfoResponse> response = redisCacheService.getCacheNames();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(CacheNames.PRODUCTS, response.get(0).getName());
        assertEquals("1 Hour", response.get(0).getConfiguredTtl());
    }

    @Test
    void testEvictCacheSuccess() {
        when(cacheManager.getCache(CacheNames.PRODUCTS)).thenReturn(sampleCache);

        redisCacheService.evictCache(CacheNames.PRODUCTS);

        verify(sampleCache, times(1)).clear();
    }

    @Test
    void testEvictCacheKeySuccess() {
        when(cacheManager.getCache(CacheNames.PRODUCT_DETAILS)).thenReturn(sampleCache);

        redisCacheService.evictCacheKey(CacheNames.PRODUCT_DETAILS, 200L);

        verify(sampleCache, times(1)).evict(200L);
    }

    @Test
    void testClearAllCachesSuccess() {
        when(cacheManager.getCacheNames()).thenReturn(Arrays.asList(CacheNames.PRODUCTS, CacheNames.CATEGORIES));
        when(cacheManager.getCache(anyString())).thenReturn(sampleCache);

        redisCacheService.clearAllCaches();

        verify(sampleCache, times(2)).clear();
    }
}
