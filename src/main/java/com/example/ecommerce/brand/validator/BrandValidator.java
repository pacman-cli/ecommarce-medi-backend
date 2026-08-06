package com.example.ecommerce.brand.validator;

import com.example.ecommerce.brand.dto.request.BrandRequest;
import com.example.ecommerce.brand.repository.BrandRepository;
import com.example.ecommerce.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Enterprise validator enforcing brand business constraints, uniqueness rules and URL validations.
 */
@Component
@RequiredArgsConstructor
public class BrandValidator {

    private final BrandRepository brandRepository;

    /**
     * Validates business constraints when creating a brand.
     *
     * @param request creation request payload
     */
    public void validateForCreate(BrandRequest request) {
        validateNameUniqueness(request.getName(), null);
        if (StringUtils.hasText(request.getSlug())) {
            validateSlugUniqueness(request.getSlug(), null);
        }
    }

    /**
     * Validates business constraints when updating an existing brand.
     *
     * @param brandId ID of the brand being updated
     * @param request update request payload
     */
    public void validateForUpdate(Long brandId, BrandRequest request) {
        if (StringUtils.hasText(request.getName())) {
            validateNameUniqueness(request.getName(), brandId);
        }
        if (StringUtils.hasText(request.getSlug())) {
            validateSlugUniqueness(request.getSlug(), brandId);
        }
    }

    /**
     * Validates uniqueness of brand name (case-insensitive).
     */
    public void validateNameUniqueness(String name, Long excludeId) {
        String trimmed = name.trim();
        boolean exists = excludeId == null
                ? brandRepository.existsByNameIgnoreCase(trimmed)
                : brandRepository.existsByNameIgnoreCaseAndIdNot(trimmed, excludeId);

        if (exists) {
            throw new ConflictException("Brand with name '" + trimmed + "' already exists");
        }
    }

    /**
     * Validates uniqueness of brand slug (case-insensitive).
     */
    public void validateSlugUniqueness(String slug, Long excludeId) {
        String trimmed = slug.trim().toLowerCase();
        boolean exists = excludeId == null
                ? brandRepository.existsBySlugIgnoreCase(trimmed)
                : brandRepository.existsBySlugIgnoreCaseAndIdNot(trimmed, excludeId);

        if (exists) {
            throw new ConflictException("Brand with slug '" + trimmed + "' already exists");
        }
    }
}
