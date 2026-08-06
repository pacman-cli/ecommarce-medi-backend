package com.example.ecommerce.search.dto.response;

import com.example.ecommerce.product.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * As-you-type quick autocomplete search results payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "As-you-type autocomplete and quick match response payload")
public class AutocompleteResponse {

    @Schema(description = "Matching product completion keywords", example = "[\"paracetamol 500mg\", \"paracetamol syrup\"]")
    private List<String> keywordSuggestions;

    @Schema(description = "Matching product preview cards")
    private List<ProductResponse> products;

    @Schema(description = "Matching category shortcuts")
    private List<CategorySuggestionDto> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Category quick suggestion item")
    public static class CategorySuggestionDto {
        private Long id;
        private String name;
        private String slug;
    }
}
