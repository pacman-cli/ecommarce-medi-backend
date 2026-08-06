package com.example.ecommerce.address.service.impl;

import com.example.ecommerce.address.dto.request.AddressRequest;
import com.example.ecommerce.address.dto.response.AddressResponse;
import com.example.ecommerce.address.mapper.AddressMapper;
import com.example.ecommerce.address.repository.AddressRepository;
import com.example.ecommerce.address.service.AddressService;
import com.example.ecommerce.address.validator.AddressValidator;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.user.entity.Address;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Service implementation managing customer addresses, administrative geography,
 * geo-coordinates, and default shipping/billing designations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
    private final AddressValidator addressValidator;

    @Override
    public AddressResponse createAddress(Long userId, AddressRequest request) {
        log.info("Creating new address for userId: {}", userId);
        addressValidator.validateRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address address = addressMapper.toEntity(request);
        address.setUser(user);

        long existingCount = addressRepository.countByUserIdAndDeletedFalse(userId);
        if (existingCount == 0) {
            address.setDefault(true);
            address.setDefaultShipping(true);
            address.setDefaultBilling(true);
        } else {
            if (Boolean.TRUE.equals(request.getDefaultShipping())) {
                addressRepository.unsetPreviousDefaultShipping(userId);
                address.setDefaultShipping(true);
                address.setDefault(true);
            }
            if (Boolean.TRUE.equals(request.getDefaultBilling())) {
                addressRepository.unsetPreviousDefaultBilling(userId);
                address.setDefaultBilling(true);
            }
        }

        Address saved = addressRepository.save(address);
        log.info("Successfully created address ID: {} for user ID: {}", saved.getId(), userId);
        return addressMapper.toResponse(saved);
    }

    @Override
    public AddressResponse updateAddress(Long id, Long userId, AddressRequest request) {
        log.info("Updating address ID: {} for userId: {}", id, userId);
        addressValidator.validateRequest(request);

        Address address = addressRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

        addressValidator.verifyOwnership(address, userId);

        if (Boolean.TRUE.equals(request.getDefaultShipping())) {
            addressRepository.unsetPreviousDefaultShipping(userId);
            address.setDefaultShipping(true);
            address.setDefault(true);
        }
        if (Boolean.TRUE.equals(request.getDefaultBilling())) {
            addressRepository.unsetPreviousDefaultBilling(userId);
            address.setDefaultBilling(true);
        }

        addressMapper.updateEntityFromRequest(request, address);
        Address updated = addressRepository.save(address);

        log.info("Successfully updated address ID: {}", updated.getId());
        return addressMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long id, Long userId) {
        Address address = addressRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
        addressValidator.verifyOwnership(address, userId);
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AddressResponse> getMyAddresses(Long userId, Pageable pageable) {
        Page<Address> page = addressRepository.findByUserIdAndDeletedFalse(userId, pageable);
        return PageResponse.from(page, addressMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddressesByUser(Long userId) {
        List<Address> addresses = addressRepository.findByUserIdAndDeletedFalse(userId);
        return addressMapper.toResponseList(addresses);
    }

    @Override
    public AddressResponse setDefaultShippingAddress(Long id, Long userId) {
        log.info("Setting address ID: {} as default shipping address for userId: {}", id, userId);
        Address address = addressRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
        addressValidator.verifyOwnership(address, userId);

        addressRepository.unsetPreviousDefaultShipping(userId);
        address.setDefaultShipping(true);
        address.setDefault(true);

        Address updated = addressRepository.save(address);
        return addressMapper.toResponse(updated);
    }

    @Override
    public AddressResponse setDefaultBillingAddress(Long id, Long userId) {
        log.info("Setting address ID: {} as default billing address for userId: {}", id, userId);
        Address address = addressRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
        addressValidator.verifyOwnership(address, userId);

        addressRepository.unsetPreviousDefaultBilling(userId);
        address.setDefaultBilling(true);

        Address updated = addressRepository.save(address);
        return addressMapper.toResponse(updated);
    }

    @Override
    public void deleteAddress(Long id, Long userId) {
        log.info("Soft deleting address ID: {} for userId: {}", id, userId);
        Address address = addressRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
        addressValidator.verifyOwnership(address, userId);

        address.setDeleted(true);
        address.setDeletedAt(Instant.now());
        addressRepository.save(address);

        log.info("Successfully soft deleted address ID: {}", id);
    }
}
