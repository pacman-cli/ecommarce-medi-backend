package com.example.ecommerce.supplier.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import com.example.ecommerce.supplier.dto.request.SupplierFilterRequest;
import com.example.ecommerce.supplier.dto.request.SupplierRequest;
import com.example.ecommerce.supplier.dto.response.SupplierDetailResponse;
import com.example.ecommerce.supplier.dto.response.SupplierProductSummaryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierPurchaseHistoryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierResponse;
import com.example.ecommerce.supplier.service.SupplierService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing vendor supplier profiles, trade licenses, TIN credentials,
 * status transitions, supplied product catalogs, and purchase receiving history.
 */
@RestController("vendorSupplierController")
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Supplier & Vendor Management", description = "Endpoints for managing vendors, company profiles, trade licenses, TIN credentials, supplied products, and purchase history")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @Operation(summary = "Create supplier profile", description = "Adds a new vendor supplier profile with company name, contact person, trade license, and TIN")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Supplier created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Duplicate supplier code, company name, or invalid input")
    })
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @Valid @RequestBody SupplierRequest request) {
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Supplier profile created successfully"));
    }

    @GetMapping
    @Operation(summary = "Search suppliers (Paginated)", description = "Retrieves paginated list of suppliers matching keyword, status, trade license, or active filters")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> getSuppliers(
            SupplierFilterRequest filter,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<SupplierResponse> page = supplierService.getSuppliers(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Suppliers retrieved successfully"));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active suppliers", description = "Retrieves complete list of active vendor suppliers for dropdown selections")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllActiveSuppliers() {
        List<SupplierResponse> list = supplierService.getAllActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(list, "Active suppliers retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID", description = "Retrieves supplier profile information by ID")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
        SupplierResponse response = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Supplier profile retrieved successfully"));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get detailed supplier profile", description = "Retrieves complete vendor profile including supplied product catalog and purchase expenditure metrics")
    public ResponseEntity<ApiResponse<SupplierDetailResponse>> getSupplierDetailById(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
        SupplierDetailResponse response = supplierService.getSupplierDetailById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Detailed supplier profile retrieved successfully"));
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Get products supplied by vendor", description = "Retrieves list of distinct products supplied by specified vendor with current stock counts")
    public ResponseEntity<ApiResponse<List<SupplierProductSummaryResponse>>> getSupplierProducts(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
        List<SupplierProductSummaryResponse> list = supplierService.getSupplierProducts(id);
        return ResponseEntity.ok(ApiResponse.success(list, "Supplied products retrieved successfully"));
    }

    @GetMapping("/{id}/purchase-history")
    @Operation(summary = "Get purchase batch receiving history", description = "Retrieves list of stock batches received from specified supplier")
    public ResponseEntity<ApiResponse<List<SupplierPurchaseHistoryResponse>>> getSupplierPurchaseHistory(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
        List<SupplierPurchaseHistoryResponse> list = supplierService.getSupplierPurchaseHistory(id);
        return ResponseEntity.ok(ApiResponse.success(list, "Purchase receiving history retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supplier profile", description = "Updates an existing vendor supplier profile")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request) {
        SupplierResponse response = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Supplier profile updated successfully"));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update supplier status lifecycle", description = "Updates supplier status (ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION)")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplierStatus(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id,
            @Parameter(description = "New supplier status", required = true) @RequestParam SupplierStatus status) {
        SupplierResponse response = supplierService.updateSupplierStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(response, "Supplier status updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete supplier profile", description = "Soft deletes a supplier profile by ID")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier profile deleted successfully"));
    }
}
