package com.example.ecommerce.search.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import com.example.ecommerce.search.dto.response.AutocompleteResponse;
import com.example.ecommerce.search.dto.response.RecentSearchResponse;
import com.example.ecommerce.search.dto.response.SearchFacetResponse;
import com.example.ecommerce.search.dto.response.SearchSuggestionResponse;
import com.example.ecommerce.search.dto.response.TrendingSearchResponse;
import com.example.ecommerce.search.entity.SearchQueryLog;
import com.example.ecommerce.search.enums.SearchSortOption;
import com.example.ecommerce.search.repository.SearchQueryLogRepository;
import com.example.ecommerce.search.service.impl.SearchServiceImpl;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Mock
    private SearchEngine searchEngine;

    @Mock
    private SearchQueryLogRepository searchQueryLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    private SearchFilterRequest filterRequest;

    @BeforeEach
    void setUp() {
        filterRequest = SearchFilterRequest.builder()
                .query("paracetamol")
                .minPrice(new BigDecimal("5.00"))
                .maxPrice(new BigDecimal("50.00"))
                .sort(SearchSortOption.PRICE_ASC)
                .page(0)
                .size(10)
                .build();
    }

    @Test
    void testSearchProducts() {
        PageResponse<ProductResponse> mockPage = PageResponse.<ProductResponse>builder()
                .content(Collections.emptyList())
                .page(0)
                .size(10)
                .totalElements(0L)
                .totalPages(0)
                .first(true)
                .last(true)
                .build();

        when(searchEngine.search(any(SearchFilterRequest.class))).thenReturn(mockPage);

        PageResponse<ProductResponse> result = searchService.searchProducts(filterRequest);

        assertNotNull(result);
        verify(searchEngine, times(1)).search(eq(filterRequest));
    }

    @Test
    void testGetSearchFacets() {
        SearchFacetResponse mockFacets = SearchFacetResponse.builder()
                .minPrice(new BigDecimal("2.00"))
                .maxPrice(new BigDecimal("150.00"))
                .build();

        when(searchEngine.getFacets(any(SearchFilterRequest.class))).thenReturn(mockFacets);

        SearchFacetResponse result = searchService.getSearchFacets(filterRequest);

        assertNotNull(result);
        assertEquals(new BigDecimal("2.00"), result.getMinPrice());
    }

    @Test
    void testAutocomplete() {
        AutocompleteResponse mockResponse = AutocompleteResponse.builder()
                .keywordSuggestions(Arrays.asList("paracetamol 500mg", "paracetamol syrup"))
                .products(Collections.emptyList())
                .categories(Collections.emptyList())
                .build();

        when(searchEngine.autocomplete(eq("para"), eq(5))).thenReturn(mockResponse);

        AutocompleteResponse result = searchService.autocomplete("para", 5);

        assertNotNull(result);
        assertEquals(2, result.getKeywordSuggestions().size());
    }

    @Test
    void testGetSearchSuggestions() {
        when(searchQueryLogRepository.findAutocompleteKeywords(eq("paracitamol"), any(Pageable.class)))
                .thenReturn(Arrays.asList("paracetamol"));

        SearchSuggestionResponse suggestions = searchService.getSearchSuggestions("paracitamol");

        assertNotNull(suggestions);
        assertEquals("paracitamol", suggestions.getOriginalQuery());
        assertEquals("paracetamol", suggestions.getDidYouMean());
    }

    @Test
    void testGetTrendingSearches() {
        List<Object[]> mockTrending = Arrays.asList(
                new Object[]{"napa extra", 150L},
                new Object[]{"vitamin c", 120L}
        );

        when(searchQueryLogRepository.findTrendingSearchTerms(any(Instant.class), any(Pageable.class)))
                .thenReturn(mockTrending);

        TrendingSearchResponse response = searchService.getTrendingSearches(10);

        assertNotNull(response);
        assertEquals(2, response.getItems().size());
        assertEquals("napa extra", response.getItems().get(0).getQuery());
        assertTrue(response.getItems().get(0).getIsHot());
    }

    @Test
    void testGetRecentSearches() {
        Object[] item1 = new Object[]{"paracetamol", Instant.now()};
        List<Object[]> mockRecent = Collections.singletonList(item1);

        when(searchQueryLogRepository.findRecentSearchesByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(mockRecent);

        RecentSearchResponse response = searchService.getRecentSearches(1L, 10);

        assertNotNull(response);
        assertEquals(1, response.getRecentSearches().size());
        assertEquals("paracetamol", response.getRecentSearches().get(0).getQuery());
    }

    @Test
    void testClearRecentSearches() {
        searchService.clearRecentSearches(1L);
        verify(searchQueryLogRepository, times(1)).deleteByUserId(eq(1L));
    }
}
