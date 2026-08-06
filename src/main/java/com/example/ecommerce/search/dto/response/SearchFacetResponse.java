package com.example.ecommerce.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Dynamic search facet aggregation metadata (category counts, brand counts, price range bounds).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dynamic facet metadata breakdown for search filters")
public class SearchFacetResponse {

    @Schema(description = "Category counts distribution (Category Name -> Product Count)")
    private Map<String, Long> categoryCounts;

    @Schema(description = "Brand counts distribution (Brand Name -> Product Count)")
    private Map<String, Long> brandCounts;

    @Schema(description = "Minimum selling price found in search results", example = "5.50")
    private BigDecimal minPrice;

    @Schema(description = "Maximum selling price found in search results", example = "450.00")
    private BigDecimal maxPrice;

    @Schema(description = "Count of items in stock", example = "142")
    private Long inStockCount;

    @Schema(description = "Count of items requiring prescription", example = "18")
    private Long prescriptionRequiredCount;

    @Schema(description = "Count of items with active discounts", example = "35")
    private Long discountedCount;
}
