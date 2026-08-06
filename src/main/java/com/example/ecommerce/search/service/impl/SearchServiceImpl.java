package com.example.ecommerce.search.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import com.example.ecommerce.search.dto.response.AutocompleteResponse;
import com.example.ecommerce.search.dto.response.RecentSearchResponse;
import com.example.ecommerce.search.dto.response.SearchFacetResponse;
import com.example.ecommerce.search.dto.response.SearchSuggestionResponse;
import com.example.ecommerce.search.dto.response.TrendingSearchResponse;
import com.example.ecommerce.search.entity.SearchQueryLog;
import com.example.ecommerce.search.repository.SearchQueryLogRepository;
import com.example.ecommerce.search.service.SearchEngine;
import com.example.ecommerce.search.service.SearchService;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation managing search execution, caching, query logging,
 * trending terms calculation, and user search history.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final SearchEngine searchEngine;
    private final SearchQueryLogRepository searchQueryLogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchProducts(SearchFilterRequest filter) {
        log.debug("Executing searchProducts with filter query: {}", filter.getQuery());
        PageResponse<ProductResponse> response = searchEngine.search(filter);

        if (StringUtils.hasText(filter.getQuery())) {
            logSearchQuery(filter.getQuery(), null, response.getTotalElements());
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public SearchFacetResponse getSearchFacets(SearchFilterRequest filter) {
        return searchEngine.getFacets(filter);
    }

    @Override
    @Cacheable(value = "searchAutocompleteCache", key = "#query + '_' + #limit", unless = "#result == null")
    @Transactional(readOnly = true)
    public AutocompleteResponse autocomplete(String query, int limit) {
        if (!StringUtils.hasText(query)) {
            return AutocompleteResponse.builder()
                    .keywordSuggestions(Collections.emptyList())
                    .products(Collections.emptyList())
                    .categories(Collections.emptyList())
                    .build();
        }
        return searchEngine.autocomplete(query.trim(), limit);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchSuggestionResponse getSearchSuggestions(String query) {
        if (!StringUtils.hasText(query)) {
            return SearchSuggestionResponse.builder()
                    .originalQuery(query)
                    .relatedQueries(Collections.emptyList())
                    .build();
        }

        String trimmed = query.trim();
        Pageable pageable = PageRequest.of(0, 5);
        List<String> keywords = searchQueryLogRepository.findAutocompleteKeywords(trimmed, pageable);

        String didYouMean = null;
        if (!keywords.isEmpty() && !keywords.get(0).equalsIgnoreCase(trimmed)) {
            didYouMean = keywords.get(0);
        }

        return SearchSuggestionResponse.builder()
                .originalQuery(trimmed)
                .didYouMean(didYouMean)
                .relatedQueries(keywords)
                .build();
    }

    @Override
    @Cacheable(value = "searchTrendingCache", key = "#limit")
    @Transactional(readOnly = true)
    public TrendingSearchResponse getTrendingSearches(int limit) {
        int targetLimit = limit > 0 ? Math.min(limit, 20) : 10;
        Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);
        Pageable pageable = PageRequest.of(0, targetLimit);

        List<Object[]> rows = searchQueryLogRepository.findTrendingSearchTerms(thirtyDaysAgo, pageable);
        List<TrendingSearchResponse.TrendingItemDto> items = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            String term = (String) row[0];
            Long count = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            items.add(TrendingSearchResponse.TrendingItemDto.builder()
                    .query(term)
                    .searchCount(count)
                    .isHot(i < 3)
                    .build());
        }

        return TrendingSearchResponse.builder()
                .items(items)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public RecentSearchResponse getRecentSearches(Long userId, int limit) {
        if (userId == null) {
            return RecentSearchResponse.builder().recentSearches(Collections.emptyList()).build();
        }

        int targetLimit = limit > 0 ? Math.min(limit, 20) : 10;
        Pageable pageable = PageRequest.of(0, targetLimit);
        List<Object[]> rows = searchQueryLogRepository.findRecentSearchesByUserId(userId, pageable);

        List<RecentSearchResponse.RecentSearchItemDto> items = rows.stream()
                .map(row -> RecentSearchResponse.RecentSearchItemDto.builder()
                        .query((String) row[0])
                        .searchedAt((Instant) row[1])
                        .build())
                .collect(Collectors.toList());

        return RecentSearchResponse.builder()
                .recentSearches(items)
                .build();
    }

    @Override
    public void clearRecentSearches(Long userId) {
        if (userId != null) {
            log.info("Clearing recent search log entries for userId: {}", userId);
            searchQueryLogRepository.deleteByUserId(userId);
        }
    }

    @Override
    public void logSearchQuery(String query, Long userId, long resultCount) {
        if (!StringUtils.hasText(query)) {
            return;
        }

        try {
            String trimmed = query.trim();
            String normalized = trimmed.toLowerCase();

            User user = null;
            if (userId != null) {
                user = userRepository.findById(userId).orElse(null);
            }

            Optional<SearchQueryLog> existingOpt = searchQueryLogRepository.findByNormalizedTermAndUserId(normalized, userId);
            if (existingOpt.isPresent()) {
                SearchQueryLog logEntry = existingOpt.get();
                logEntry.setSearchCount(logEntry.getSearchCount() + 1);
                logEntry.setResultCount(resultCount);
                logEntry.setLastSearchedAt(Instant.now());
                searchQueryLogRepository.save(logEntry);
            } else {
                SearchQueryLog logEntry = SearchQueryLog.builder()
                        .queryTerm(trimmed)
                        .normalizedTerm(normalized)
                        .user(user)
                        .resultCount(resultCount)
                        .searchCount(1L)
                        .lastSearchedAt(Instant.now())
                        .build();
                searchQueryLogRepository.save(logEntry);
            }
        } catch (Exception e) {
            log.warn("Failed to log search query term: {} - {}", query, e.getMessage());
        }
    }
}
