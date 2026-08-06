package com.example.ecommerce.common.util;

import com.example.ecommerce.common.constant.AppConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.regex.Pattern;

/**
 * Utility responsible for building validated {@link Pageable} instances from raw
 * controller query parameters.
 */
public final class PageUtils {

    private static final Pattern SAFE_SORT_PROPERTY = Pattern.compile("[a-zA-Z0-9_.]+");

    private PageUtils() {
    }

    /**
     * Builds a {@link Pageable} enforcing sane bounds and rejecting malicious
     * sort expressions.
     *
     * @param page    the zero-based page index
     * @param size    the requested page size
     * @param sortBy  the entity property to sort by
     * @param sortDir the sort direction ({@code asc} or {@code desc})
     * @return a validated pageable
     */
    public static Pageable createPageable(int page, int size, String sortBy, String sortDir) {
        int resolvedPage = Math.max(page, 0);
        int resolvedSize = Math.min(Math.max(size, 1), AppConstants.MAX_PAGE_SIZE);
        Sort.Direction direction = AppConstants.SORT_DIRECTION_DESC.equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(resolvedPage, resolvedSize, Sort.by(direction, resolveSortProperty(sortBy)));
    }

    /**
     * Falls back to the default sort property when the requested one is missing
     * or does not match the whitelist pattern.
     *
     * @param sortBy the requested sort property
     * @return a safe sort property
     */
    private static String resolveSortProperty(String sortBy) {
        if (sortBy == null || sortBy.isBlank() || !SAFE_SORT_PROPERTY.matcher(sortBy).matches()) {
            return AppConstants.DEFAULT_SORT_BY;
        }
        return sortBy;
    }
}
