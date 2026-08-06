package com.example.ecommerce.search.dto.request;

import com.example.ecommerce.search.enums.SearchSortOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Filter and search criteria request DTO for querying product catalogue.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Search criteria parameters payload for multi-attribute product search")
public class SearchFilterRequest {

    @Schema(description = "Free-text search query string", example = "paracetamol 500mg")
    private String query;

    @Schema(description = "Filter by list of category IDs", example = "[1, 2, 5]")
    private List<Long> categoryIds;

    @Schema(description = "Filter by single category URL slug", example = "analgesics")
    private String categorySlug;

    @Schema(description = "Filter by list of brand IDs", example = "[10, 12]")
    private List<Long> brandIds;

    @Schema(description = "Filter by single brand URL slug", example = "square-pharma")
    private String brandSlug;

    @Schema(description = "Minimum selling price filter", example = "10.00")
    @Min(value = 0, message = "Minimum price cannot be negative")
    private BigDecimal minPrice;

    @Schema(description = "Maximum selling price filter", example = "250.00")
    private BigDecimal maxPrice;

    @Schema(description = "Filter for in-stock products only", example = "true")
    private Boolean inStockOnly;

    @Schema(description = "Filter for products currently on discount", example = "true")
    private Boolean hasDiscount;

    @Schema(description = "Filter for products requiring medical prescription", example = "false")
    private Boolean prescriptionRequired;

    @Schema(description = "Filter by minimum customer rating score (1.0 to 5.0)", example = "4.0")
    @Min(value = 1, message = "Minimum rating must be at least 1")
    @Max(value = 5, message = "Maximum rating cannot exceed 5")
    private Double minRating;

    @Schema(description = "Sort ordering option", example = "PRICE_ASC")
    @Builder.Default
    private SearchSortOption sort = SearchSortOption.RELEVANCE;

    @Schema(description = "Zero-based page index", example = "0")
    @Min(value = 0, message = "Page index cannot be negative")
    @Builder.Default
    private Integer page = 0;

    @Schema(description = "Number of items per page", example = "20")
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    @Builder.Default
    private Integer size = 20;
}
