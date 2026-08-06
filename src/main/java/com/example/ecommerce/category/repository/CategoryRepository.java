package com.example.ecommerce.category.repository;

import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.entity.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Category} entities with specification support.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    /**
     * Finds a non-deleted category by ID.
     */
    Optional<Category> findByIdAndDeletedFalse(Long id);

    /**
     * Finds an active category by ID.
     */
    Optional<Category> findByIdAndActiveTrue(Long id);

    /**
     * Finds a category by its URL slug.
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Finds a non-deleted category by its URL slug.
     */
    Optional<Category> findBySlugAndDeletedFalse(String slug);

    /**
     * Checks if a category name already exists (case-insensitive).
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Checks if a category name already exists excluding a specific ID.
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    /**
     * Checks if a slug already exists (case-insensitive).
     */
    boolean existsBySlugIgnoreCase(String slug);

    /**
     * Checks if a slug already exists excluding a specific ID.
     */
    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);

    /**
     * Returns top-level root categories (where parent is null).
     */
    List<Category> findByParentIsNullAndDeletedFalseOrderBySortOrderAscNameAsc();

    /**
     * Returns direct child categories for a given parent ID.
     */
    List<Category> findByParentIdAndDeletedFalseOrderBySortOrderAscNameAsc(Long parentId);

    /**
     * Returns featured categories filtered by status.
     */
    List<Category> findByFeaturedTrueAndStatusAndDeletedFalseOrderBySortOrderAscNameAsc(CategoryStatus status);

    /**
     * Counts active direct children under a parent category.
     */
    long countByParentIdAndDeletedFalse(Long parentId);

    /**
     * Eagerly fetches all root categories along with their immediate children.
     */
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL AND c.deleted = false ORDER BY c.sortOrder ASC, c.name ASC")
    List<Category> findRootCategoriesWithChildren();
}
