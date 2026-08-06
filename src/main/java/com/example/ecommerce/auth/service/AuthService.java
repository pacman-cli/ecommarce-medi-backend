package com.example.ecommerce.auth.service;

import com.example.ecommerce.auth.dto.request.ChangePasswordRequest;
import com.example.ecommerce.auth.dto.request.ForgotPasswordRequest;
import com.example.ecommerce.auth.dto.request.LoginRequest;
import com.example.ecommerce.auth.dto.request.RefreshTokenRequest;
import com.example.ecommerce.auth.dto.request.RegisterRequest;
import com.example.ecommerce.auth.dto.request.ResendVerificationRequest;
import com.example.ecommerce.auth.dto.request.ResetPasswordRequest;
import com.example.ecommerce.auth.dto.request.VerifyEmailRequest;
import com.example.ecommerce.auth.dto.response.AuthResponse;
import com.example.ecommerce.user.dto.response.UserResponse;

/**
 * Contract for authentication and account-recovery operations.
 */
public interface AuthService {

    /**
     * Registers a new customer account and issues a token pair to log them in.
     *
     * @param request the registration payload
     * @return the token pair and profile
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates a user by credentials and returns a token pair.
     *
     * @param request the login payload
     * @return the token pair and profile
     */
    AuthResponse login(LoginRequest request);

    /**
     * Rotates a valid refresh token into a fresh token pair, revoking the
     * previous session and rejecting reuse of stale tokens.
     *
     * @param request the refresh token payload
     * @return the new token pair and profile
     */
    AuthResponse refresh(RefreshTokenRequest request);

    /**
     * Revokes the active session: deletes the refresh session and blacklists
     * the presented access token.
     *
     * @param accessToken the access token to blacklist
     */
    void logout(String accessToken);

    /**
     * Confirms an email address using a one-time verification code.
     *
     * @param request the email and code payload
     */
    void verifyEmail(VerifyEmailRequest request);

    /**
     * Re-issues an email verification code for an unverified account.
     *
     * @param request the email payload
     */
    void resendVerification(ResendVerificationRequest request);

    /**
     * Begins the password reset flow by issuing and emailing a reset code.
     *
     * @param request the email payload
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * Resets a password after validating a reset code, revoking all sessions.
     *
     * @param request the email, code and new password payload
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param request     the current and new password payload
     * @param accessToken the current access token (revoked after the change)
     */
    void changePassword(ChangePasswordRequest request, String accessToken);

    /**
     * Returns the profile of the currently authenticated user.
     *
     * @return the user profile
     */
    UserResponse getCurrentUser();
}