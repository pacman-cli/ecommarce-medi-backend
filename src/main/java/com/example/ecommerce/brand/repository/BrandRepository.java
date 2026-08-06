package com.example.ecommerce.brand.repository;

import com.example.ecommerce.brand.entity.Brand;
import com.example.ecommerce.brand.entity.BrandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Brand} entities with JPA Specification execution support.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {

    /**
     * Finds a non-deleted brand by ID.
     */
    Optional<Brand> findByIdAndDeletedFalse(Long id);

    /**
     * Finds a brand by URL slug.
     */
    Optional<Brand> findBySlug(String slug);

    /**
     * Finds a non-deleted brand by URL slug.
     */
    Optional<Brand> findBySlugAndDeletedFalse(String slug);

    /**
     * Checks if a brand name already exists (case-insensitive).
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Checks if a brand name already exists excluding a specific ID.
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    /**
     * Checks if a brand slug already exists (case-insensitive).
     */
    boolean existsBySlugIgnoreCase(String slug);

    /**
     * Checks if a brand slug already exists excluding a specific ID.
     */
    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);

    /**
     * Returns featured active brands sorted by sort order and name.
     */
    List<Brand> findByFeaturedTrueAndStatusAndDeletedFalseOrderBySortOrderAscNameAsc(BrandStatus status);

    /**
     * Returns all active brands sorted by sort order and name.
     */
    List<Brand> findByStatusAndDeletedFalseOrderBySortOrderAscNameAsc(BrandStatus status);
}
