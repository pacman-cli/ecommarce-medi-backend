package com.example.ecommerce.address.controller;

import com.example.ecommerce.address.dto.request.AddressRequest;
import com.example.ecommerce.address.dto.response.AddressResponse;
import com.example.ecommerce.address.service.AddressService;
import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.security.UserPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing customer shipping, billing, geography, and default address operations.
 */
@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Customer Addresses", description = "Endpoints for managing customer shipping & billing addresses, divisions, districts, coordinates, and default designations")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @Operation(summary = "Create customer address", description = "Adds a new customer shipping or billing address with administrative geography and GPS coordinates")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Address created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address payload or coordinate boundaries")
    })
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AddressRequest request) {
        Long userId = userPrincipal.getUser().getId();
        AddressResponse response = addressService.createAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Address created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get user addresses (Paginated)", description = "Retrieves paginated list of active customer addresses for authenticated user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<AddressResponse>>> getMyAddresses(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = userPrincipal.getUser().getId();
        PageResponse<AddressResponse> page = addressService.getMyAddresses(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Addresses retrieved successfully"));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all user addresses", description = "Retrieves complete list of active addresses for authenticated user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All addresses retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAllMyAddresses(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUser().getId();
        List<AddressResponse> list = addressService.getAllAddressesByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(list, "All addresses retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get address by ID", description = "Retrieves single customer address details by ID verifying ownership")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "Address ID", required = true) @PathVariable Long id) {
        Long userId = userPrincipal.getUser().getId();
        AddressResponse response = addressService.getAddressById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Address details retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer address", description = "Updates an existing customer shipping or billing address")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "Address ID", required = true) @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        Long userId = userPrincipal.getUser().getId();
        AddressResponse response = addressService.updateAddress(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Address updated successfully"));
    }

    @PutMapping("/{id}/default-shipping")
    @Operation(summary = "Set default shipping address", description = "Designates address as default shipping address and unsets previous default")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Default shipping address set successfully")
    })
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultShippingAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "Address ID", required = true) @PathVariable Long id) {
        Long userId = userPrincipal.getUser().getId();
        AddressResponse response = addressService.setDefaultShippingAddress(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Default shipping address set successfully"));
    }

    @PutMapping("/{id}/default-billing")
    @Operation(summary = "Set default billing address", description = "Designates address as default billing address and unsets previous default")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Default billing address set successfully")
    })
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultBillingAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "Address ID", required = true) @PathVariable Long id) {
        Long userId = userPrincipal.getUser().getId();
        AddressResponse response = addressService.setDefaultBillingAddress(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Default billing address set successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer address", description = "Soft deletes a customer address by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "Address ID", required = true) @PathVariable Long id) {
        Long userId = userPrincipal.getUser().getId();
        addressService.deleteAddress(id, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Address deleted successfully"));
    }
}
