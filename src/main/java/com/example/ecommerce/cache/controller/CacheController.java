package com.example.ecommerce.cache.controller;

import com.example.ecommerce.cache.dto.response.CacheRegionInfoResponse;
import com.example.ecommerce.cache.service.RedisCacheService;
import com.example.ecommerce.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for inspecting active Redis cache regions and manually triggering cache evictions or flushes.
 */
@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Redis Cache Management", description = "Endpoints for inspecting active Redis cache regions and triggering cache evictions")
public class CacheController {

    private final RedisCacheService redisCacheService;

    @GetMapping("/names")
    @Operation(summary = "Get active cache regions", description = "Retrieves all configured Redis cache region names and their TTL policies")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cache regions retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<CacheRegionInfoResponse>>> getCacheNames() {
        List<CacheRegionInfoResponse> caches = redisCacheService.getCacheNames();
        return ResponseEntity.ok(ApiResponse.success(caches, "Cache regions retrieved successfully"));
    }

    @DeleteMapping("/{cacheName}")
    @Operation(summary = "Evict cache region", description = "Clears and evicts all cached entries within specified cache region")
    public ResponseEntity<ApiResponse<Void>> evictCache(
            @Parameter(description = "Cache region name", required = true) @PathVariable String cacheName) {
        redisCacheService.evictCache(cacheName);
        return ResponseEntity.ok(ApiResponse.success(null, "Cache region '" + cacheName + "' evicted successfully"));
    }

    @DeleteMapping("/{cacheName}/{key}")
    @Operation(summary = "Evict cache key", description = "Evicts a single specific key entry from a cache region")
    public ResponseEntity<ApiResponse<Void>> evictCacheKey(
            @Parameter(description = "Cache region name", required = true) @PathVariable String cacheName,
            @Parameter(description = "Cache key", required = true) @PathVariable String key) {
        redisCacheService.evictCacheKey(cacheName, key);
        return ResponseEntity.ok(ApiResponse.success(null, "Cache key '" + key + "' evicted successfully from region '" + cacheName + "'"));
    }

    @PostMapping("/clear-all")
    @Operation(summary = "Clear all caches", description = "Flushes and clears all application Redis cache regions")
    public ResponseEntity<ApiResponse<Void>> clearAllCaches() {
        redisCacheService.clearAllCaches();
        return ResponseEntity.ok(ApiResponse.success(null, "All application caches cleared successfully"));
    }
}
