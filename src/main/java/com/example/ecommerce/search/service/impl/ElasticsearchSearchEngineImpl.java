package com.example.ecommerce.search.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import com.example.ecommerce.search.dto.response.AutocompleteResponse;
import com.example.ecommerce.search.dto.response.SearchFacetResponse;
import com.example.ecommerce.search.service.SearchEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Strategy implementation of {@link SearchEngine} for Elasticsearch clusters.
 * Activated when application property {@code search.engine=elasticsearch} is set.
 */
@Slf4j
@Service("elasticsearchSearchEngine")
@ConditionalOnProperty(name = "search.engine", havingValue = "elasticsearch")
@RequiredArgsConstructor
public class ElasticsearchSearchEngineImpl implements SearchEngine {

    @Override
    public PageResponse<ProductResponse> search(SearchFilterRequest filter) {
        log.info("Executing Elasticsearch cluster product query for: {}", filter.getQuery());
        // Pluggable strategy stub - delegating cluster query execution
        throw new UnsupportedOperationException("Elasticsearch cluster connection is not configured yet. Please use database search engine.");
    }

    @Override
    public SearchFacetResponse getFacets(SearchFilterRequest filter) {
        log.info("Computing Elasticsearch aggregations for: {}", filter);
        throw new UnsupportedOperationException("Elasticsearch facet aggregation is not configured yet.");
    }

    @Override
    public AutocompleteResponse autocomplete(String query, int limit) {
        log.info("Executing Elasticsearch completion suggest query for: {}", query);
        throw new UnsupportedOperationException("Elasticsearch completion suggest is not configured yet.");
    }
}
