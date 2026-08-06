package com.example.ecommerce.brand.controller;

import com.example.ecommerce.brand.dto.request.BrandFilterRequest;
import com.example.ecommerce.brand.dto.request.BrandRequest;
import com.example.ecommerce.brand.dto.response.BrandResponse;
import com.example.ecommerce.brand.entity.BrandStatus;
import com.example.ecommerce.brand.service.BrandService;
import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing endpoints for brand management, search, dynamic filtering,
 * logo/banner links, and CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Tag(name = "Brand Management", description = "Endpoints for managing product brands, logo/banner metadata, search and soft deletion")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "Search and filter brands", description = "Returns a paginated list of brands based on dynamic criteria")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brands retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<BrandResponse>>> getBrands(
            @ModelAttribute BrandFilterRequest filter,
            @PageableDefault(sort = "sortOrder", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<BrandResponse> page = brandService.getBrands(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Brands retrieved successfully"));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured brands", description = "Returns active brands marked as featured")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Featured brands retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getFeaturedBrands() {
        List<BrandResponse> featured = brandService.getFeaturedBrands();
        return ResponseEntity.ok(ApiResponse.success(featured, "Featured brands retrieved successfully"));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active brands", description = "Returns complete unpaginated list of active brands")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active brands retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllActiveBrands() {
        List<BrandResponse> activeBrands = brandService.getAllActiveBrands();
        return ResponseEntity.ok(ApiResponse.success(activeBrands, "Active brands retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID", description = "Retrieves brand details by unique ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brand retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(
            @Parameter(description = "Brand ID", required = true) @PathVariable Long id) {
        BrandResponse brand = brandService.getBrandById(id);
        return ResponseEntity.ok(ApiResponse.success(brand, "Brand retrieved successfully"));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get brand by URL slug", description = "Retrieves brand details by URL slug")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brand retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandBySlug(
            @Parameter(description = "Brand slug", required = true) @PathVariable String slug) {
        BrandResponse brand = brandService.getBrandBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(brand, "Brand retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create brand", description = "Creates a new brand (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Brand created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Brand name or slug conflict")
    })
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(
            @Valid @RequestBody BrandRequest request) {
        BrandResponse created = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Brand created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update brand", description = "Updates brand details, logo/banner images, website URL or country (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brand updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Brand not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Brand name or slug conflict")
    })
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @Parameter(description = "Brand ID", required = true) @PathVariable Long id,
            @Valid @RequestBody BrandRequest request) {
        BrandResponse updated = brandService.updateBrand(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Brand updated successfully"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update brand status", description = "Toggles brand status (ACTIVE/INACTIVE) (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brand status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<ApiResponse<BrandResponse>> updateStatus(
            @Parameter(description = "Brand ID", required = true) @PathVariable Long id,
            @Parameter(description = "New Brand Status", required = true) @RequestParam BrandStatus status) {
        BrandResponse updated = brandService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Brand status updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete brand", description = "Soft deletes a brand (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Brand soft deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteBrand(
            @Parameter(description = "Brand ID", required = true) @PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Brand soft deleted successfully"));
    }
}
