package com.example.ecommerce.address.service;

import com.example.ecommerce.address.dto.request.AddressRequest;
import com.example.ecommerce.address.dto.response.AddressResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface defining business operations for customer shipping and billing addresses.
 */
public interface AddressService {

    /**
     * Creates a new address for specified user.
     */
    AddressResponse createAddress(Long userId, AddressRequest request);

    /**
     * Updates an existing address for specified user.
     */
    AddressResponse updateAddress(Long id, Long userId, AddressRequest request);

    /**
     * Retrieves address by ID verifying user ownership.
     */
    AddressResponse getAddressById(Long id, Long userId);

    /**
     * Retrieves paginated list of active addresses for specified user.
     */
    PageResponse<AddressResponse> getMyAddresses(Long userId, Pageable pageable);

    /**
     * Retrieves all active addresses for specified user.
     */
    List<AddressResponse> getAllAddressesByUser(Long userId);

    /**
     * Designates specified address as the default shipping address for user.
     */
    AddressResponse setDefaultShippingAddress(Long id, Long userId);

    /**
     * Designates specified address as the default billing address for user.
     */
    AddressResponse setDefaultBillingAddress(Long id, Long userId);

    /**
     * Soft deletes specified customer address.
     */
    void deleteAddress(Long id, Long userId);
}
