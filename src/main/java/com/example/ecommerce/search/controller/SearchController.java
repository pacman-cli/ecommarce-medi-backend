package com.example.ecommerce.search.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import com.example.ecommerce.search.dto.response.AutocompleteResponse;
import com.example.ecommerce.search.dto.response.RecentSearchResponse;
import com.example.ecommerce.search.dto.response.SearchFacetResponse;
import com.example.ecommerce.search.dto.response.SearchSuggestionResponse;
import com.example.ecommerce.search.dto.response.TrendingSearchResponse;
import com.example.ecommerce.search.service.SearchService;
import com.example.ecommerce.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing endpoints for product search, autocomplete, filters, suggestions,
 * trending search analytics, and user recent search queries.
 */
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Product Search & Discovery", description = "Endpoints for multi-attribute search, as-you-type autocomplete, filtering, sorting, suggestions, and trending searches")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "Search products", description = "Executes multi-attribute product search with dynamic filtering, sorting, price range, and pagination")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @Valid @ModelAttribute SearchFilterRequest filter) {
        PageResponse<ProductResponse> response = searchService.searchProducts(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Search results retrieved successfully"));
    }

    @GetMapping("/facets")
    @Operation(summary = "Get search facets", description = "Retrieves category counts, brand counts, and price min/max bounds for filter UI controls")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search facets retrieved successfully")
    })
    public ResponseEntity<ApiResponse<SearchFacetResponse>> getSearchFacets(
            @Valid @ModelAttribute SearchFilterRequest filter) {
        SearchFacetResponse facets = searchService.getSearchFacets(filter);
        return ResponseEntity.ok(ApiResponse.success(facets, "Search facets retrieved successfully"));
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "As-you-type autocomplete", description = "Fast quick suggestions returning matching keywords, product cards, and categories")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Autocomplete suggestions retrieved successfully")
    })
    public ResponseEntity<ApiResponse<AutocompleteResponse>> autocomplete(
            @Parameter(description = "Search prefix query", required = true, example = "para")
            @RequestParam String q,
            @Parameter(description = "Max items limit", example = "5")
            @RequestParam(defaultValue = "5") int limit) {
        AutocompleteResponse response = searchService.autocomplete(q, limit);
        return ResponseEntity.ok(ApiResponse.success(response, "Autocomplete suggestions retrieved successfully"));
    }

    @GetMapping("/suggestions")
    @Operation(summary = "Get search suggestions", description = "Retrieves related search queries and did-you-mean recommendations")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search suggestions retrieved successfully")
    })
    public ResponseEntity<ApiResponse<SearchSuggestionResponse>> getSearchSuggestions(
            @Parameter(description = "Original query term", required = true, example = "paracitamol")
            @RequestParam String q) {
        SearchSuggestionResponse response = searchService.getSearchSuggestions(q);
        return ResponseEntity.ok(ApiResponse.success(response, "Search suggestions retrieved successfully"));
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending searches", description = "Retrieves popular search terms ordered by search volume frequency")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trending searches retrieved successfully")
    })
    public ResponseEntity<ApiResponse<TrendingSearchResponse>> getTrendingSearches(
            @Parameter(description = "Maximum limit", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        TrendingSearchResponse response = searchService.getTrendingSearches(limit);
        return ResponseEntity.ok(ApiResponse.success(response, "Trending searches retrieved successfully"));
    }

    @GetMapping("/recent")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get user recent searches", description = "Retrieves current authenticated user's recent search queries")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recent searches retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Bearer token required")
    })
    public ResponseEntity<ApiResponse<RecentSearchResponse>> getRecentSearches(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "Maximum limit", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        Long userId = userPrincipal != null && userPrincipal.getUser() != null ? userPrincipal.getUser().getId() : null;
        RecentSearchResponse response = searchService.getRecentSearches(userId, limit);
        return ResponseEntity.ok(ApiResponse.success(response, "Recent searches retrieved successfully"));
    }

    @DeleteMapping("/recent")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Clear user recent searches", description = "Clears recent search history for current authenticated user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recent searches cleared successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Bearer token required")
    })
    public ResponseEntity<ApiResponse<Void>> clearRecentSearches(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal != null && userPrincipal.getUser() != null) {
            searchService.clearRecentSearches(userPrincipal.getUser().getId());
        }
        return ResponseEntity.ok(ApiResponse.success(null, "Recent searches cleared successfully"));
    }
}
