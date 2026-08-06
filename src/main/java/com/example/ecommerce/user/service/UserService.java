package com.example.ecommerce.user.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.user.dto.request.AddressRequest;
import com.example.ecommerce.user.dto.request.UpdateProfileRequest;
import com.example.ecommerce.user.dto.request.UpdateRoleRequest;
import com.example.ecommerce.user.dto.request.UpdateStatusRequest;
import com.example.ecommerce.user.dto.request.UserFilterRequest;
import com.example.ecommerce.user.dto.response.AddressResponse;
import com.example.ecommerce.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Contract for user-management, self-service profile and address operations.
 */
public interface UserService {

    PageResponse<UserResponse> getAllUsers(UserFilterRequest filter, int page, int size, String sortBy, String sortDir);

    UserResponse getUserById(Long id);

    UserResponse updateRole(Long id, UpdateRoleRequest request);

    UserResponse updateStatus(Long id, UpdateStatusRequest request);

    void deactivateUser(Long id);

    UserResponse getMyProfile();

    UserResponse updateMyProfile(UpdateProfileRequest request);

    UserResponse uploadProfileImage(MultipartFile file);

    List<AddressResponse> getMyAddresses();

    AddressResponse addAddress(AddressRequest request);

    AddressResponse updateAddress(Long addressId, AddressRequest request);

    void deleteAddress(Long addressId);
}
