package com.example.ecommerce.brand.service.impl;

import com.example.ecommerce.brand.dto.request.BrandFilterRequest;
import com.example.ecommerce.brand.dto.request.BrandRequest;
import com.example.ecommerce.brand.dto.response.BrandResponse;
import com.example.ecommerce.brand.entity.Brand;
import com.example.ecommerce.brand.entity.BrandStatus;
import com.example.ecommerce.brand.mapper.BrandMapper;
import com.example.ecommerce.brand.repository.BrandRepository;
import com.example.ecommerce.brand.service.BrandService;
import com.example.ecommerce.brand.specification.BrandSpecification;
import com.example.ecommerce.brand.validator.BrandValidator;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Service implementation managing brand business workflows, web-friendly slug generation,
 * dynamic specification search, status updates and soft deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern DUPLICATE_HYPHENS = Pattern.compile("-+");

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final BrandValidator brandValidator;

    @Override
    @Transactional
    public BrandResponse createBrand(BrandRequest request) {
        log.info("Creating brand with name: {}", request.getName());
        brandValidator.validateForCreate(request);

        Brand brand = brandMapper.toEntity(request);

        // Auto-generate slug if omitted
        if (!StringUtils.hasText(brand.getSlug())) {
            brand.setSlug(generateUniqueSlug(request.getName(), null));
        } else {
            brand.setSlug(brand.getSlug().toLowerCase(Locale.ROOT).trim());
        }

        if (brand.getStatus() == null) {
            brand.setStatus(BrandStatus.ACTIVE);
        }
        if (brand.getSortOrder() == null) {
            brand.setSortOrder(0);
        }
        brand.setActive(brand.getStatus() == BrandStatus.ACTIVE);

        Brand savedBrand = brandRepository.save(brand);
        log.info("Successfully created brand with ID: {}", savedBrand.getId());
        return brandMapper.toResponse(savedBrand);
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(Long id, BrandRequest request) {
        log.info("Updating brand ID: {}", id);
        Brand brand = findBrandEntityById(id);

        brandValidator.validateForUpdate(id, request);

        boolean nameChanged = StringUtils.hasText(request.getName()) && !request.getName().trim().equals(brand.getName());

        brandMapper.updateEntityFromRequest(request, brand);

        if (nameChanged && !StringUtils.hasText(request.getSlug())) {
            brand.setSlug(generateUniqueSlug(request.getName(), id));
        } else if (StringUtils.hasText(request.getSlug())) {
            brand.setSlug(request.getSlug().toLowerCase(Locale.ROOT).trim());
        }

        if (request.getStatus() != null) {
            brand.setActive(request.getStatus() == BrandStatus.ACTIVE);
        }

        Brand updatedBrand = brandRepository.save(brand);
        log.info("Successfully updated brand ID: {}", updatedBrand.getId());
        return brandMapper.toResponse(updatedBrand);
    }

    @Override
    public BrandResponse getBrandById(Long id) {
        Brand brand = findBrandEntityById(id);
        return brandMapper.toResponse(brand);
    }

    @Override
    public BrandResponse getBrandBySlug(String slug) {
        Brand brand = brandRepository.findBySlugAndDeletedFalse(slug.toLowerCase(Locale.ROOT).trim())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with slug: " + slug));
        return brandMapper.toResponse(brand);
    }

    @Override
    public PageResponse<BrandResponse> getBrands(BrandFilterRequest filter, Pageable pageable) {
        Specification<Brand> spec = BrandSpecification.build(filter);
        Page<Brand> brandPage = brandRepository.findAll(spec, pageable);
        return PageResponse.from(brandPage, brandMapper::toResponse);
    }

    @Override
    public List<BrandResponse> getFeaturedBrands() {
        List<Brand> featured = brandRepository.findByFeaturedTrueAndStatusAndDeletedFalseOrderBySortOrderAscNameAsc(BrandStatus.ACTIVE);
        return brandMapper.toResponseList(featured);
    }

    @Override
    public List<BrandResponse> getAllActiveBrands() {
        List<Brand> activeBrands = brandRepository.findByStatusAndDeletedFalseOrderBySortOrderAscNameAsc(BrandStatus.ACTIVE);
        return brandMapper.toResponseList(activeBrands);
    }

    @Override
    @Transactional
    public BrandResponse updateStatus(Long id, BrandStatus status) {
        log.info("Updating brand ID {} status to: {}", id, status);
        Brand brand = findBrandEntityById(id);
        brand.setStatus(status);
        brand.setActive(status == BrandStatus.ACTIVE);
        Brand updated = brandRepository.save(brand);
        return brandMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        log.info("Soft deleting brand ID: {}", id);
        Brand brand = findBrandEntityById(id);
        brand.setDeleted(true);
        brand.setDeletedAt(Instant.now());
        brand.setStatus(BrandStatus.INACTIVE);
        brand.setActive(false);
        brandRepository.save(brand);
        log.info("Successfully soft deleted brand ID: {}", id);
    }

    private Brand findBrandEntityById(Long id) {
        return brandRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
    }

    private String generateUniqueSlug(String text, Long excludeId) {
        String baseSlug = toSlug(text);
        if (!StringUtils.hasText(baseSlug)) {
            baseSlug = "brand-" + System.currentTimeMillis();
        }

        String candidateSlug = baseSlug;
        int counter = 1;
        while (isSlugTaken(candidateSlug, excludeId)) {
            candidateSlug = baseSlug + "-" + counter;
            counter++;
        }
        return candidateSlug;
    }

    private boolean isSlugTaken(String slug, Long excludeId) {
        return excludeId == null
                ? brandRepository.existsBySlugIgnoreCase(slug)
                : brandRepository.existsBySlugIgnoreCaseAndIdNot(slug, excludeId);
    }

    private static String toSlug(String input) {
        if (input == null) {
            return "";
        }
        String nowhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        slug = DUPLICATE_HYPHENS.matcher(slug).replaceAll("-");
        return slug.toLowerCase(Locale.ROOT).replaceAll("^-+|-+$", "");
    }
}
