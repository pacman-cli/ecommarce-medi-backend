package com.example.ecommerce.brand.service;

import com.example.ecommerce.brand.dto.request.BrandFilterRequest;
import com.example.ecommerce.brand.dto.request.BrandRequest;
import com.example.ecommerce.brand.dto.response.BrandResponse;
import com.example.ecommerce.brand.entity.BrandStatus;
import com.example.ecommerce.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface defining business operations for brand management.
 */
public interface BrandService {

    /**
     * Creates a new brand.
     *
     * @param request creation request payload
     * @return created brand response DTO
     */
    BrandResponse createBrand(BrandRequest request);

    /**
     * Updates an existing brand by ID.
     *
     * @param id      brand ID
     * @param request update payload
     * @return updated brand response DTO
     */
    BrandResponse updateBrand(Long id, BrandRequest request);

    /**
     * Retrieves brand by ID.
     *
     * @param id brand ID
     * @return brand response DTO
     */
    BrandResponse getBrandById(Long id);

    /**
     * Retrieves brand by URL slug.
     *
     * @param slug brand slug
     * @return brand response DTO
     */
    BrandResponse getBrandBySlug(String slug);

    /**
     * Searches brands with dynamic filtering and pagination.
     *
     * @param filter   filter criteria
     * @param pageable pagination parameters
     * @return paginated brand response
     */
    PageResponse<BrandResponse> getBrands(BrandFilterRequest filter, Pageable pageable);

    /**
     * Returns list of featured active brands.
     *
     * @return featured brands list
     */
    List<BrandResponse> getFeaturedBrands();

    /**
     * Returns list of all active brands.
     *
     * @return active brands list
     */
    List<BrandResponse> getAllActiveBrands();

    /**
     * Updates brand operational status (ACTIVE/INACTIVE).
     *
     * @param id     brand ID
     * @param status new status
     * @return updated brand response DTO
     */
    BrandResponse updateStatus(Long id, BrandStatus status);

    /**
     * Soft deletes brand.
     *
     * @param id brand ID
     */
    void deleteBrand(Long id);
}
