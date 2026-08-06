package com.example.ecommerce.search.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import com.example.ecommerce.search.dto.response.AutocompleteResponse;
import com.example.ecommerce.search.dto.response.RecentSearchResponse;
import com.example.ecommerce.search.dto.response.SearchFacetResponse;
import com.example.ecommerce.search.dto.response.SearchSuggestionResponse;
import com.example.ecommerce.search.dto.response.TrendingSearchResponse;

/**
 * High-level search service interface orchestrating multi-engine searching,
 * autocomplete, suggestions, query logging, trending searches, and user history.
 */
public interface SearchService {

    /**
     * Executes product search matching dynamic filter criteria.
     */
    PageResponse<ProductResponse> searchProducts(SearchFilterRequest filter);

    /**
     * Retrieves search facet breakdown (category, brand, price min/max).
     */
    SearchFacetResponse getSearchFacets(SearchFilterRequest filter);

    /**
     * As-you-type quick autocomplete suggestions.
     */
    AutocompleteResponse autocomplete(String query, int limit);

    /**
     * Retrieves related search suggestions & did-you-mean phrase corrections.
     */
    SearchSuggestionResponse getSearchSuggestions(String query);

    /**
     * Retrieves trending popular search queries.
     */
    TrendingSearchResponse getTrendingSearches(int limit);

    /**
     * Retrieves authenticated user's recent search queries.
     */
    RecentSearchResponse getRecentSearches(Long userId, int limit);

    /**
     * Clears recent search history for user.
     */
    void clearRecentSearches(Long userId);

    /**
     * Logs search query execution for analytics and trending calculations.
     */
    void logSearchQuery(String query, Long userId, long resultCount);
}
