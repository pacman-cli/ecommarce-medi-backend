package com.example.ecommerce.coupon.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.coupon.dto.request.CouponFilterRequest;
import com.example.ecommerce.coupon.dto.request.CouponRequest;
import com.example.ecommerce.coupon.dto.request.ValidateCouponRequest;
import com.example.ecommerce.coupon.dto.response.CouponResponse;
import com.example.ecommerce.coupon.dto.response.CouponValidationResponse;
import com.example.ecommerce.coupon.service.CouponService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller exposing endpoints for coupon administration, validation engine evaluations,
 * automatic coupon matching, and redemption verification.
 */
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon Management", description = "Endpoints for promotional coupon CRUD, validation engine, automatic coupons and checkout redemptions")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create coupon", description = "Creates a new promotional coupon with percentage or fixed discount rules (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Coupon created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload or date range"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Coupon code already exists")
    })
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(
            @Valid @RequestBody CouponRequest request) {
        CouponResponse created = couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Coupon created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update coupon", description = "Updates an existing promotional coupon (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coupon updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<ApiResponse<CouponResponse>> updateCoupon(
            @Parameter(description = "Coupon ID", required = true) @PathVariable Long id,
            @Valid @RequestBody CouponRequest request) {
        CouponResponse updated = couponService.updateCoupon(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Coupon updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get coupon by ID", description = "Retrieves coupon details by ID (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coupon retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<ApiResponse<CouponResponse>> getCouponById(
            @Parameter(description = "Coupon ID", required = true) @PathVariable Long id) {
        CouponResponse coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(ApiResponse.success(coupon, "Coupon retrieved successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get paginated coupons", description = "Retrieves paginated list of coupons with dynamic search and status filtering (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coupons retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<CouponResponse>>> getCoupons(
            @ModelAttribute CouponFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<CouponResponse> page = couponService.getCoupons(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Coupons retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete coupon", description = "Soft deletes a promotional coupon (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coupon deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(
            @Parameter(description = "Coupon ID", required = true) @PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Coupon deleted successfully"));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate coupon code", description = "Evaluates coupon code against cart subtotal, usage limits and date windows")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coupon validation outcome returned")
    })
    public ResponseEntity<ApiResponse<CouponValidationResponse>> validateCoupon(
            @Valid @RequestBody ValidateCouponRequest request) {
        CouponValidationResponse outcome = couponService.validateCoupon(request);
        return ResponseEntity.ok(ApiResponse.success(outcome, "Coupon validation completed"));
    }

    @GetMapping("/automatic")
    @Operation(summary = "Get automatic coupons", description = "Returns active automatic coupons applicable to a given cart subtotal")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applicable automatic coupons retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getAutomaticCoupons(
            @Parameter(description = "Cart subtotal amount", example = "100.00") @RequestParam BigDecimal subtotal) {
        List<CouponResponse> autoCoupons = couponService.getApplicableAutomaticCoupons(subtotal);
        return ResponseEntity.ok(ApiResponse.success(autoCoupons, "Applicable automatic coupons retrieved successfully"));
    }
}
