package com.example.ecommerce.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Did-you-mean and related phrase recommendations payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Related search suggestions and did-you-mean recommendations")
public class SearchSuggestionResponse {

    @Schema(description = "Original query term", example = "paracitamol")
    private String originalQuery;

    @Schema(description = "Did you mean auto-corrected query phrase", example = "paracetamol")
    private String didYouMean;

    @Schema(description = "Related search query terms")
    private List<String> relatedQueries;
}
