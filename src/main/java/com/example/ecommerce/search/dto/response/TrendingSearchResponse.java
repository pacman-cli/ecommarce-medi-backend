package com.example.ecommerce.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Trending popular search keywords payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Popular trending search queries payload")
public class TrendingSearchResponse {

    @Schema(description = "List of trending search keyword items")
    private List<TrendingItemDto> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Trending search term item")
    public static class TrendingItemDto {
        private String query;
        private Long searchCount;
        private Boolean isHot;
    }
}
