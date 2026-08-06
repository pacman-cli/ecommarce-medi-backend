package com.example.ecommerce.auth.dto.response;

import com.example.ecommerce.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token pair plus the authenticated user profile returned on login, register
 * and refresh.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresInMs;
    private UserResponse user;
}
