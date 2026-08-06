package com.example.ecommerce.category.service.impl;

import com.example.ecommerce.category.dto.request.CategoryFilterRequest;
import com.example.ecommerce.category.dto.request.CategoryRequest;
import com.example.ecommerce.category.dto.response.CategoryResponse;
import com.example.ecommerce.category.dto.response.CategoryTreeResponse;
import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.entity.CategoryStatus;
import com.example.ecommerce.category.mapper.CategoryMapper;
import com.example.ecommerce.category.repository.CategoryRepository;
import com.example.ecommerce.category.service.CategoryService;
import com.example.ecommerce.category.specification.CategorySpecification;
import com.example.ecommerce.category.validator.CategoryValidator;
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
 * Service implementation for managing product categories with unlimited hierarchy,
 * dynamic slug generation, filtering, soft deletion, and status management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern DUPLICATE_HYPHENS = Pattern.compile("-+");

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidator categoryValidator;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating category with name: {}", request.getName());
        categoryValidator.validateForCreate(request);

        Category category = categoryMapper.toEntity(request);

        // Handle slug generation if not explicitly provided
        if (!StringUtils.hasText(category.getSlug())) {
            category.setSlug(generateUniqueSlug(request.getName(), null));
        } else {
            category.setSlug(category.getSlug().toLowerCase(Locale.ROOT).trim());
        }

        // Parent binding
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findByIdAndDeletedFalse(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with ID: " + request.getParentId()));
            category.setParent(parent);
        }

        // Apply defaults
        if (category.getStatus() == null) {
            category.setStatus(CategoryStatus.ACTIVE);
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        category.setActive(category.getStatus() == CategoryStatus.ACTIVE);

        Category savedCategory = categoryRepository.save(category);
        log.info("Successfully created category with ID: {}", savedCategory.getId());
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category ID: {}", id);
        Category category = findCategoryEntityById(id);

        categoryValidator.validateForUpdate(id, request);

        // Track if name changed and slug was omitted to auto-update slug
        boolean nameChanged = StringUtils.hasText(request.getName()) && !request.getName().trim().equals(category.getName());

        categoryMapper.updateEntityFromRequest(request, category);

        if (nameChanged && !StringUtils.hasText(request.getSlug())) {
            category.setSlug(generateUniqueSlug(request.getName(), id));
        } else if (StringUtils.hasText(request.getSlug())) {
            category.setSlug(request.getSlug().toLowerCase(Locale.ROOT).trim());
        }

        // Update parent relationship if explicitly specified in payload
        if (request.getParentId() != null) {
            if (category.getParent() == null || !request.getParentId().equals(category.getParent().getId())) {
                Category parent = categoryRepository.findByIdAndDeletedFalse(request.getParentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with ID: " + request.getParentId()));
                category.setParent(parent);
            }
        } else if (request.getParentId() == null && request.getSortOrder() != null) {
            // Null parentId means convert to root category if parent was set before
            category.setParent(null);
        }

        if (request.getStatus() != null) {
            category.setActive(request.getStatus() == CategoryStatus.ACTIVE);
        }

        Category updatedCategory = categoryRepository.save(category);
        log.info("Successfully updated category ID: {}", updatedCategory.getId());
        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategoryEntityById(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlugAndDeletedFalse(slug.toLowerCase(Locale.ROOT).trim())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return categoryMapper.toResponse(category);
    }

    @Override
    public PageResponse<CategoryResponse> getCategories(CategoryFilterRequest filter, Pageable pageable) {
        Specification<Category> spec = CategorySpecification.build(filter);
        Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);
        return PageResponse.from(categoryPage, categoryMapper::toResponse);
    }

    @Override
    public List<CategoryTreeResponse> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findRootCategoriesWithChildren();
        return categoryMapper.toTreeResponseList(rootCategories);
    }

    @Override
    public List<CategoryResponse> getRootCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIsNullAndDeletedFalseOrderBySortOrderAscNameAsc();
        return categoryMapper.toResponseList(rootCategories);
    }

    @Override
    public List<CategoryResponse> getSubCategories(Long parentId) {
        findCategoryEntityById(parentId); // Validate parent exists
        List<Category> subCategories = categoryRepository.findByParentIdAndDeletedFalseOrderBySortOrderAscNameAsc(parentId);
        return categoryMapper.toResponseList(subCategories);
    }

    @Override
    public List<CategoryResponse> getFeaturedCategories() {
        List<Category> featured = categoryRepository.findByFeaturedTrueAndStatusAndDeletedFalseOrderBySortOrderAscNameAsc(CategoryStatus.ACTIVE);
        return categoryMapper.toResponseList(featured);
    }

    @Override
    @Transactional
    public CategoryResponse updateStatus(Long id, CategoryStatus status) {
        log.info("Updating category ID {} status to: {}", id, status);
        Category category = findCategoryEntityById(id);
        category.setStatus(status);
        category.setActive(status == CategoryStatus.ACTIVE);
        Category updated = categoryRepository.save(category);
        return categoryMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Soft deleting category ID: {}", id);
        Category category = findCategoryEntityById(id);
        softDeleteRecursive(category);
        log.info("Successfully soft deleted category ID: {}", id);
    }

    /**
     * Recursively soft-deletes a category and its entire descendant hierarchy.
     */
    private void softDeleteRecursive(Category category) {
        category.setDeleted(true);
        category.setDeletedAt(Instant.now());
        category.setStatus(CategoryStatus.INACTIVE);
        category.setActive(false);

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            for (Category child : category.getChildren()) {
                softDeleteRecursive(child);
            }
        }
        categoryRepository.save(category);
    }

    private Category findCategoryEntityById(Long id) {
        return categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    /**
     * Generates a web-friendly unique URL slug from a given text.
     */
    private String generateUniqueSlug(String text, Long excludeId) {
        String baseSlug = toSlug(text);
        if (!StringUtils.hasText(baseSlug)) {
            baseSlug = "category-" + System.currentTimeMillis();
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
                ? categoryRepository.existsBySlugIgnoreCase(slug)
                : categoryRepository.existsBySlugIgnoreCaseAndIdNot(slug, excludeId);
    }

    /**
     * Normalizes text into standard URL slug format.
     */
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
