package com.example.ecommerce.inventory.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.inventory.dto.request.SupplierRequest;
import com.example.ecommerce.inventory.dto.response.SupplierResponse;
import com.example.ecommerce.inventory.service.SupplierService;
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
 * REST controller for supplier and vendor management.
 */
@RestController("inventorySupplierController")
@RequestMapping("/api/v1/inventory/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier Management", description = "Endpoints for managing vendors, suppliers and contact details")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    @Operation(summary = "Get suppliers", description = "Returns a paginated list of suppliers with optional keyword search")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> getSuppliers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean activeOnly,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<SupplierResponse> page = supplierService.getSuppliers(search, activeOnly, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Suppliers retrieved successfully"));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active suppliers", description = "Returns complete list of active suppliers")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active suppliers retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllActiveSuppliers() {
        List<SupplierResponse> list = supplierService.getAllActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(list, "Active suppliers retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID", description = "Retrieves supplier details by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Supplier retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
        SupplierResponse response = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Supplier retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get supplier by code", description = "Retrieves supplier details by code")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Supplier retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierByCode(
            @Parameter(description = "Supplier code", required = true) @PathVariable String code) {
        SupplierResponse response = supplierService.getSupplierByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response, "Supplier retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create supplier", description = "Creates a new supplier vendor (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Supplier created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Code or name conflict")
    })
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @Valid @RequestBody SupplierRequest request) {
        SupplierResponse created = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Supplier created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update supplier", description = "Updates supplier details (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request) {
        SupplierResponse updated = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Supplier updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete supplier", description = "Soft deletes a supplier vendor (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Supplier deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(
            @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier soft deleted successfully"));
    }
}
