package com.example.ecommerce.search.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import com.example.ecommerce.search.dto.response.AutocompleteResponse;
import com.example.ecommerce.search.dto.response.SearchFacetResponse;

/**
 * Strategy interface contract for product search engines (Database vs Elasticsearch).
 */
public interface SearchEngine {

    /**
     * Executes product search matching incoming filter criteria.
     */
    PageResponse<ProductResponse> search(SearchFilterRequest filter);

    /**
     * Computes search facet aggregations (category breakdown, brand breakdown, price min/max).
     */
    SearchFacetResponse getFacets(SearchFilterRequest filter);

    /**
     * Executes as-you-type quick autocomplete.
     */
    AutocompleteResponse autocomplete(String query, int limit);
}
