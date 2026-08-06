package com.example.ecommerce.inventory.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.inventory.dto.request.WarehouseRequest;
import com.example.ecommerce.inventory.dto.response.WarehouseResponse;
import com.example.ecommerce.inventory.service.WarehouseService;
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
 * REST controller for warehouse location and storage facility management.
 */
@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse Management", description = "Endpoints for managing physical warehouses, capacity and locations")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    @Operation(summary = "Get warehouses", description = "Returns a paginated list of warehouses with optional keyword search")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Warehouses retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<WarehouseResponse>>> getWarehouses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean activeOnly,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<WarehouseResponse> page = warehouseService.getWarehouses(search, activeOnly, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Warehouses retrieved successfully"));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active warehouses", description = "Returns complete list of active warehouses")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active warehouses retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> getAllActiveWarehouses() {
        List<WarehouseResponse> list = warehouseService.getAllActiveWarehouses();
        return ResponseEntity.ok(ApiResponse.success(list, "Active warehouses retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID", description = "Retrieves warehouse details by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Warehouse retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseById(
            @Parameter(description = "Warehouse ID", required = true) @PathVariable Long id) {
        WarehouseResponse response = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Warehouse retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get warehouse by code", description = "Retrieves warehouse details by code")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Warehouse retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseByCode(
            @Parameter(description = "Warehouse code", required = true) @PathVariable String code) {
        WarehouseResponse response = warehouseService.getWarehouseByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response, "Warehouse retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create warehouse", description = "Creates a new warehouse facility (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Warehouse created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Code or name conflict")
    })
    public ResponseEntity<ApiResponse<WarehouseResponse>> createWarehouse(
            @Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse created = warehouseService.createWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Warehouse created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update warehouse", description = "Updates warehouse details (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Warehouse updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    public ResponseEntity<ApiResponse<WarehouseResponse>> updateWarehouse(
            @Parameter(description = "Warehouse ID", required = true) @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse updated = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Warehouse updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete warehouse", description = "Soft deletes a warehouse facility (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Warehouse deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(
            @Parameter(description = "Warehouse ID", required = true) @PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Warehouse soft deleted successfully"));
    }
}
