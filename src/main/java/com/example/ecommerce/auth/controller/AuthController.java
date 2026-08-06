package com.example.ecommerce.auth.controller;

import com.example.ecommerce.auth.dto.request.ChangePasswordRequest;
import com.example.ecommerce.auth.dto.request.ForgotPasswordRequest;
import com.example.ecommerce.auth.dto.request.LoginRequest;
import com.example.ecommerce.auth.dto.request.RefreshTokenRequest;
import com.example.ecommerce.auth.dto.request.RegisterRequest;
import com.example.ecommerce.auth.dto.request.ResendVerificationRequest;
import com.example.ecommerce.auth.dto.request.ResetPasswordRequest;
import com.example.ecommerce.auth.dto.request.VerifyEmailRequest;
import com.example.ecommerce.auth.dto.response.AuthResponse;
import com.example.ecommerce.auth.service.AuthService;
import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints: register, login, refresh, logout, email
 * verification and password recovery.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Registration, login, token refresh, email verification and password recovery")
public class AuthController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;

    /**
     * Creates a new customer account and returns a token pair.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new customer account")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(authService.register(request), "Registration successful");
    }

    /**
     * Authenticates a user and returns a token pair.
     */
    @PostMapping("/login")
    @Operation(summary = "Login and obtain access/refresh tokens")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request), "Login successful");
    }

    /**
     * Rotates a refresh token into a new token pair.
     */
    @PostMapping("/refresh")
    @Operation(summary = "Exchange a refresh token for a new token pair")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(authService.refresh(request), "Token refreshed successfully");
    }

    /**
     * Confirms an email address with a one-time code.
     */
    @PostMapping("/verify-email")
    @Operation(summary = "Verify an email address with a one-time code")
    public ApiResponse<Void> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        return ApiResponse.success("Email verified successfully");
    }

    /**
     * Re-issues an email verification code.
     */
    @PostMapping("/resend-verification")
    @Operation(summary = "Re-issue an email verification code")
    public ApiResponse<Void> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        authService.resendVerification(request);
        return ApiResponse.success("Verification code sent successfully");
    }

    /**
     * Starts the password reset flow by emailing a reset code.
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Request a password reset code")
    public ApiResponse<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.success("If the account exists, a reset code has been sent");
    }

    /**
     * Resets a password using the emailed code.
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Reset a password with a reset code")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.success("Password reset successfully");
    }

    /**
     * Changes the password of the currently authenticated user.
     */
    @PostMapping("/change-password")
    @Operation(summary = "Change the current user's password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                            HttpServletRequest httpRequest) {
        authService.changePassword(request, extractBearerToken(httpRequest));
        return ApiResponse.success("Password changed successfully");
    }

    /**
     * Revokes the current session.
     */
    @PostMapping("/logout")
    @Operation(summary = "Log out and revoke the active session")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        authService.logout(extractBearerToken(request));
        return ApiResponse.success("Logged out successfully");
    }

    /**
     * Returns the profile of the currently authenticated user.
     */
    @GetMapping("/me")
    @Operation(summary = "Return the currently authenticated user")
    public ApiResponse<UserResponse> me() {
        return ApiResponse.success(authService.getCurrentUser(), "Current user retrieved successfully");
    }

    /**
     * Extracts the raw Bearer token from the Authorization header.
     *
     * @param request the HTTP request
     * @return the token or {@code null}
     */
    private String extractBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}