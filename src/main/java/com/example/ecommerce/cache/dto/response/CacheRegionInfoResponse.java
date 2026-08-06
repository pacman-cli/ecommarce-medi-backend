package com.example.ecommerce.cache.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Active cache region metadata response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Redis cache region details payload")
public class CacheRegionInfoResponse {

    @Schema(description = "Cache region name", example = "products")
    private String name;

    @Schema(description = "Configured TTL duration description", example = "1 hour")
    private String configuredTtl;
}
