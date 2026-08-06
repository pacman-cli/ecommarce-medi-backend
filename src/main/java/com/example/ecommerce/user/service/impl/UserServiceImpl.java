package com.example.ecommerce.user.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.common.storage.StorageService;
import com.example.ecommerce.common.util.PageUtils;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.UnauthorizedException;
import com.example.ecommerce.security.UserPrincipal;
import com.example.ecommerce.user.dto.request.AddressRequest;
import com.example.ecommerce.user.dto.request.UpdateProfileRequest;
import com.example.ecommerce.user.dto.request.UpdateRoleRequest;
import com.example.ecommerce.user.dto.request.UpdateStatusRequest;
import com.example.ecommerce.user.dto.request.UserFilterRequest;
import com.example.ecommerce.user.dto.response.AddressResponse;
import com.example.ecommerce.user.dto.response.UserResponse;
import com.example.ecommerce.user.entity.Address;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.mapper.AddressMapper;
import com.example.ecommerce.user.mapper.UserMapper;
import com.example.ecommerce.user.repository.AddressRepository;
import com.example.ecommerce.user.repository.UserRepository;
import com.example.ecommerce.user.service.UserService;
import com.example.ecommerce.user.specification.UserSpecification;
import com.example.ecommerce.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link UserService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final UserValidator userValidator;
    private final StorageService storageService;

    /* ------------------------------ Admin CRUD ------------------------------ */

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(UserFilterRequest filter, int page, int size,
                                                  String sortBy, String sortDir) {
        Pageable pageable = PageUtils.createPageable(page, size, sortBy, sortDir);
        Page<User> result = UserSpecification.hasCriteria(filter)
                ? userRepository.findAll(UserSpecification.build(filter), pageable)
                : userRepository.findAll(pageable);
        return PageResponse.from(result, userMapper::toResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserResponse updateRole(Long id, UpdateRoleRequest request) {
        User user = findUser(id);
        if (user.getId().equals(currentUser().getId())) {
            throw new BadRequestException("You cannot change your own role");
        }
        user.setRole(request.getRole());
        User updated = userRepository.save(user);
        log.info("Role updated for user id={} to {}", id, request.getRole());
        return userMapper.toResponse(updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserResponse updateStatus(Long id, UpdateStatusRequest request) {
        User user = findUser(id);
        if (user.getId().equals(currentUser().getId()) && !request.getEnabled()) {
            throw new BadRequestException("You cannot disable your own account");
        }
        user.setEnabled(request.getEnabled());
        User updated = userRepository.save(user);
        log.info("Account status updated for user id={} enabled={}", id, request.getEnabled());
        return userMapper.toResponse(updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = findUser(id);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("User deactivated id={}", id);
    }

    /* ----------------------------- Self-service ----------------------------- */

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getMyProfile() {
        return userMapper.toResponse(currentUser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserResponse updateMyProfile(UpdateProfileRequest request) {
        User user = currentUser();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPhone(request.getPhone());
        User updated = userRepository.save(user);
        log.info("Profile updated for user id={}", user.getId());
        return userMapper.toResponse(updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserResponse uploadProfileImage(MultipartFile file) {
        userValidator.validateProfileImage(file);
        User user = currentUser();
        String key = storageService.store(file, "profiles");
        // Replace the existing image to avoid orphaned files.
        if (user.getProfileImageKey() != null) {
            storageService.delete(user.getProfileImageKey());
        }
        user.setProfileImageKey(key);
        userRepository.save(user);
        log.info("Profile image uploaded for user id={}", user.getId());
        return userMapper.toResponse(user);
    }

    /* ------------------------------ Addresses ------------------------------- */

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {
        User user = currentUser();
        return addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(user.getId()).stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AddressResponse addAddress(AddressRequest request) {
        User user = currentUser();
        List<Address> existing = new ArrayList<>(
                addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(user.getId()));
        boolean firstAddress = existing.isEmpty();
        boolean makeDefault = request.isDefault() || firstAddress;
        if (makeDefault) {
            addressRepository.clearDefaultForUser(user.getId());
        }
        Address address = addressMapper.toEntity(request, user);
        address.setDefault(makeDefault);
        address = addressRepository.save(address);
        log.info("Address added for user id={}, id={}", user.getId(), address.getId());
        return addressMapper.toResponse(address);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        User user = currentUser();
        Address address = findUserAddress(addressId, user.getId());
        boolean defaultCandidate = request.isDefault();
        if (defaultCandidate) {
            addressRepository.clearDefaultForUser(user.getId());
        }
        addressMapper.updateEntity(address, request);
        // Keep a sensible invariant: a sole default address must not lose its flag.
        if (!defaultCandidate && address.isDefault() && hasSingleAddress(user.getId(), addressId)) {
            address.setDefault(true);
        }
        address = addressRepository.save(address);
        log.info("Address updated for user id={}, id={}", user.getId(), addressId);
        return addressMapper.toResponse(address);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        User user = currentUser();
        Address address = findUserAddress(addressId, user.getId());
        boolean wasDefault = address.isDefault();
        addressRepository.delete(address);
        // Reassign the default flag to a remaining address, if any.
        if (wasDefault) {
            addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(user.getId()).stream()
                    .findFirst()
                    .ifPresent(candidate -> {
                        candidate.setDefault(true);
                        addressRepository.save(candidate);
                    });
        }
        log.info("Address deleted for user id={}, id={}", user.getId(), addressId);
    }

    /**
     * Resolves a user by id or throws {@link ResourceNotFoundException}.
     *
     * @param id the user id
     * @return the persisted user
     */
    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    /**
     * Resolves an address owned by the user or throws {@link ResourceNotFoundException}.
     *
     * @param addressId the address id
     * @param userId    the owning user id
     * @return the persisted address
     */
    private Address findUserAddress(Long addressId, Long userId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
    }

    /**
     * Returns whether this is currently the user's only address.
     *
     * @param userId    the owning user id
     * @param addressId the candidate address id
     * @return {@code true} when it is the sole address
     */
    private boolean hasSingleAddress(Long userId, Long addressId) {
        List<Address> all = addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(userId);
        return all.size() == 1 && all.get(0).getId().equals(addressId);
    }

    /**
     * Resolves the currently authenticated user entity.
     *
     * @return the persisted user
     */
    private User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthorizedException("No authenticated user found");
        }
        return principal.getUser();
    }
}