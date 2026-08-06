package com.example.ecommerce.user.mapper;

import com.example.ecommerce.config.properties.StorageProperties;
import com.example.ecommerce.user.dto.response.AddressResponse;
import com.example.ecommerce.user.dto.response.UserResponse;
import com.example.ecommerce.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Translates {@link User} entities into {@link UserResponse} DTOs.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final AddressMapper addressMapper;
    private final StorageProperties storageProperties;

    /**
     * Maps a user entity to a response DTO, deliberately omitting the password.
     *
     * @param user the persisted user
     * @return the response DTO
     */
    public UserResponse toResponse(User user) {
        String profileImageUrl = user.getProfileImageKey() == null
                ? null
                : storageProperties.getBaseUrl() + "/files/" + user.getProfileImageKey();
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .profileImageUrl(profileImageUrl)
                .role(user.getRole())
                .enabled(user.isEnabled())
                .emailVerified(user.isEmailVerified())
                .passwordChangedAt(user.getPasswordChangedAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
