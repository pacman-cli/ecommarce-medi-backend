package com.example.ecommerce.user.dto.response;

import com.example.ecommerce.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Public user projection returned by the API. Never exposes the password hash.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String profileImageUrl;
    private Role role;
    private boolean enabled;
    private boolean emailVerified;
    private Instant passwordChangedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
