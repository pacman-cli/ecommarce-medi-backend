package com.example.ecommerce.user.controller;

import com.example.ecommerce.common.constant.AppConstants;
import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.user.dto.request.AddressRequest;
import com.example.ecommerce.user.dto.request.UpdateProfileRequest;
import com.example.ecommerce.user.dto.request.UpdateRoleRequest;
import com.example.ecommerce.user.dto.request.UpdateStatusRequest;
import com.example.ecommerce.user.dto.request.UserFilterRequest;
import com.example.ecommerce.user.dto.response.AddressResponse;
import com.example.ecommerce.user.dto.response.UserResponse;
import com.example.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * User management (admin) and self-service profile/address endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User administration and self-service profile management")
public class UserController {

    private final UserService userService;

    /* ------------------------------ Admin CRUD ------------------------------ */

    /**
     * Lists users with pagination, sorting and dynamic filtering.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_READ')")
    @Operation(summary = "List users", description = "Admin only. Filter by search, role and status.")
    public ApiResponse<PageResponse<UserResponse>> getUsers(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir,
            @ParameterObject UserFilterRequest filter) {
        return ApiResponse.success(
                userService.getAllUsers(filter, page, size, sortBy, sortDir),
                "Users retrieved successfully");
    }

    /**
     * Returns a single user by id.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_READ')")
    @Operation(summary = "Get user by id", description = "Admin only")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id), "User retrieved successfully");
    }

    /**
     * Updates the role of a user.
     */
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_WRITE')")
    @Operation(summary = "Update user role", description = "Admin only")
    public ApiResponse<UserResponse> updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
        return ApiResponse.success(userService.updateRole(id, request), "User role updated successfully");
    }

    /**
     * Enables or disables a user account.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_WRITE')")
    @Operation(summary = "Update user account status", description = "Admin only")
    public ApiResponse<UserResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return ApiResponse.success(userService.updateStatus(id, request), "User status updated successfully");
    }

    /**
     * Soft-deactivates a user account.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_WRITE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deactivate user", description = "Admin only")
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

    /* ----------------------------- Self-service ----------------------------- */

    /**
     * Returns the currently authenticated user's profile.
     */
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('PROFILE_READ')")
    @Operation(summary = "Get my profile")
    public ApiResponse<UserResponse> getMyProfile() {
        return ApiResponse.success(userService.getMyProfile(), "Profile retrieved successfully");
    }

    /**
     * Updates the currently authenticated user's profile.
     */
    @PutMapping("/me")
    @PreAuthorize("hasAuthority('PROFILE_WRITE')")
    @Operation(summary = "Update my profile")
    public ApiResponse<UserResponse> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.success(userService.updateMyProfile(request), "Profile updated successfully");
    }

    /**
     * Uploads a profile image for the current user.
     */
    @PostMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PROFILE_WRITE')")
    @Operation(summary = "Upload my profile image")
    public ApiResponse<UserResponse> uploadProfileImage(@RequestPart("file") MultipartFile file) {
        return ApiResponse.success(userService.uploadProfileImage(file), "Profile image uploaded successfully");
    }

    /* ------------------------------ Addresses ------------------------------- */

    /**
     * Returns all addresses of the current user.
     */
    @GetMapping("/me/addresses")
    @PreAuthorize("hasAuthority('ADDRESS_READ')")
    @Operation(summary = "List my addresses")
    public ApiResponse<List<AddressResponse>> getMyAddresses() {
        return ApiResponse.success(userService.getMyAddresses(), "Addresses retrieved successfully");
    }

    /**
     * Adds an address for the current user.
     */
    @PostMapping("/me/addresses")
    @PreAuthorize("hasAuthority('ADDRESS_WRITE')")
    @Operation(summary = "Add an address")
    public ApiResponse<AddressResponse> addAddress(@Valid @RequestBody AddressRequest request) {
        return ApiResponse.success(userService.addAddress(request), "Address added successfully");
    }

    /**
     * Updates an address of the current user.
     */
    @PutMapping("/me/addresses/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_WRITE')")
    @Operation(summary = "Update an address")
    public ApiResponse<AddressResponse> updateAddress(@PathVariable Long addressId,
                                                      @Valid @RequestBody AddressRequest request) {
        return ApiResponse.success(userService.updateAddress(addressId, request), "Address updated successfully");
    }

    /**
     * Deletes an address of the current user.
     */
    @DeleteMapping("/me/addresses/{addressId}")
    @PreAuthorize("hasAuthority('ADDRESS_WRITE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an address")
    public void deleteAddress(@PathVariable Long addressId) {
        userService.deleteAddress(addressId);
    }
}
