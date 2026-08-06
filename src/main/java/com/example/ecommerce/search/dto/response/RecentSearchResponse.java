package com.example.ecommerce.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * User's recent search queries response payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User recent search query history payload")
public class RecentSearchResponse {

    @Schema(description = "List of recent search entries")
    private List<RecentSearchItemDto> recentSearches;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Recent search query item")
    public static class RecentSearchItemDto {
        private String query;
        private Instant searchedAt;
    }
}
