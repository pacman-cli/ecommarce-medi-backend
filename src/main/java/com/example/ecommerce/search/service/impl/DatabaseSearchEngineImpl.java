package com.example.ecommerce.search.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.mapper.ProductMapper;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import com.example.ecommerce.search.dto.response.AutocompleteResponse;
import com.example.ecommerce.search.dto.response.SearchFacetResponse;
import com.example.ecommerce.search.enums.SearchSortOption;
import com.example.ecommerce.search.repository.SearchProductRepository;
import com.example.ecommerce.search.service.SearchEngine;
import com.example.ecommerce.search.specification.SearchSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JPA Criteria & Specification implementation of {@link SearchEngine}.
 */
@Slf4j
@Primary
@Service("databaseSearchEngine")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DatabaseSearchEngineImpl implements SearchEngine {

    private final SearchProductRepository searchProductRepository;
    private final ProductMapper productMapper;

    @Override
    public PageResponse<ProductResponse> search(SearchFilterRequest filter) {
        log.debug("Executing database search with filter: {}", filter);
        Specification<Product> spec = SearchSpecification.build(filter);
        Pageable pageable = buildPageable(filter);

        Page<Product> page = searchProductRepository.findAll(spec, pageable);
        return PageResponse.from(page, productMapper::toResponse);
    }

    @Override
    public SearchFacetResponse getFacets(SearchFilterRequest filter) {
        log.debug("Computing search facets");
        BigDecimal minPrice = searchProductRepository.findOverallMinPrice();
        BigDecimal maxPrice = searchProductRepository.findOverallMaxPrice();

        Map<String, Long> categoryMap = new HashMap<>();
        List<Object[]> catRows = searchProductRepository.countProductsByCategory();
        for (Object[] row : catRows) {
            categoryMap.put((String) row[0], ((Number) row[1]).longValue());
        }

        Map<String, Long> brandMap = new HashMap<>();
        List<Object[]> brandRows = searchProductRepository.countProductsByBrand();
        for (Object[] row : brandRows) {
            brandMap.put((String) row[0], ((Number) row[1]).longValue());
        }

        return SearchFacetResponse.builder()
                .categoryCounts(categoryMap)
                .brandCounts(brandMap)
                .minPrice(minPrice != null ? minPrice : BigDecimal.ZERO)
                .maxPrice(maxPrice != null ? maxPrice : BigDecimal.ZERO)
                .build();
    }

    @Override
    public AutocompleteResponse autocomplete(String query, int limit) {
        log.debug("Executing database autocomplete for query: {}", query);
        int targetLimit = limit > 0 ? Math.min(limit, 20) : 5;
        Pageable pageable = PageRequest.of(0, targetLimit);

        List<String> keywords = searchProductRepository.findMatchingProductNames(query, pageable);

        SearchFilterRequest filter = SearchFilterRequest.builder()
                .query(query)
                .page(0)
                .size(targetLimit)
                .build();

        Specification<Product> spec = SearchSpecification.build(filter);
        Page<Product> productPage = searchProductRepository.findAll(spec, pageable);
        List<ProductResponse> productDtos = productMapper.toResponseList(productPage.getContent());

        List<AutocompleteResponse.CategorySuggestionDto> categoryDtos = new ArrayList<>();
        for (Product p : productPage.getContent()) {
            if (p.getCategory() != null) {
                categoryDtos.add(AutocompleteResponse.CategorySuggestionDto.builder()
                        .id(p.getCategory().getId())
                        .name(p.getCategory().getName())
                        .slug(p.getCategory().getSlug())
                        .build());
            }
        }

        List<AutocompleteResponse.CategorySuggestionDto> distinctCategories = categoryDtos.stream()
                .distinct()
                .limit(5)
                .collect(Collectors.toList());

        return AutocompleteResponse.builder()
                .keywordSuggestions(keywords)
                .products(productDtos)
                .categories(distinctCategories)
                .build();
    }

    private Pageable buildPageable(SearchFilterRequest filter) {
        int page = (filter != null && filter.getPage() != null) ? filter.getPage() : 0;
        int size = (filter != null && filter.getSize() != null) ? filter.getSize() : 20;

        SearchSortOption sortOption = (filter != null && filter.getSort() != null) ? filter.getSort() : SearchSortOption.RELEVANCE;

        Sort sort = switch (sortOption) {
            case PRICE_ASC -> Sort.by(Sort.Direction.ASC, "sellingPrice");
            case PRICE_DESC -> Sort.by(Sort.Direction.DESC, "sellingPrice");
            case NEWEST -> Sort.by(Sort.Direction.DESC, "createdAt");
            case BEST_SELLING -> Sort.by(Sort.Direction.DESC, "bestseller").and(Sort.by(Sort.Direction.DESC, "id"));
            case HIGHEST_RATED -> Sort.by(Sort.Direction.DESC, "featured").and(Sort.by(Sort.Direction.DESC, "id"));
            case RELEVANCE -> Sort.by(Sort.Direction.DESC, "id");
        };

        return PageRequest.of(page, size, sort);
    }
}
