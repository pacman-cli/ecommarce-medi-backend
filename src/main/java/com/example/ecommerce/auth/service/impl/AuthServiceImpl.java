package com.example.ecommerce.auth.service.impl;

import com.example.ecommerce.auth.dto.request.ChangePasswordRequest;
import com.example.ecommerce.auth.dto.request.ForgotPasswordRequest;
import com.example.ecommerce.auth.dto.request.LoginRequest;
import com.example.ecommerce.auth.dto.request.RefreshTokenRequest;
import com.example.ecommerce.auth.dto.request.RegisterRequest;
import com.example.ecommerce.auth.dto.request.ResendVerificationRequest;
import com.example.ecommerce.auth.dto.request.ResetPasswordRequest;
import com.example.ecommerce.auth.dto.request.VerifyEmailRequest;
import com.example.ecommerce.auth.dto.response.AuthResponse;
import com.example.ecommerce.auth.entity.VerificationCodeType;
import com.example.ecommerce.auth.service.AuthService;
import com.example.ecommerce.auth.service.VerificationCodeService;
import com.example.ecommerce.common.email.EmailService;
import com.example.ecommerce.common.email.VerificationPurpose;
import com.example.ecommerce.config.properties.JwtProperties;
import com.example.ecommerce.config.properties.VerificationProperties;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.exception.EmailNotVerifiedException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.UnauthorizedException;
import com.example.ecommerce.security.JwtService;
import com.example.ecommerce.security.LoginAttemptService;
import com.example.ecommerce.security.TokenBlacklistService;
import com.example.ecommerce.security.UserPrincipal;
import com.example.ecommerce.user.dto.response.UserResponse;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.mapper.UserMapper;
import com.example.ecommerce.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Locale;

/**
 * Implementation of {@link AuthService}.
 *
 * <p>Orchestrates registration, login (with brute-force lockout), JWT issuance,
 * refresh-token rotation with reuse detection, email verification, password
 * reset and password change.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final TokenBlacklistService tokenBlacklistService;
    private final VerificationCodeService verificationCodeService;
    private final VerificationProperties verificationProperties;
    private final EmailService emailService;
    private final LoginAttemptService loginAttemptService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("An account already exists for email: " + email);
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);
        // The account starts unverified; a code is dispatched for email confirmation.
        sendVerificationCode(user, VerificationCodeType.EMAIL_VERIFICATION);
        log.info("New user registered: {}", email);
        return buildAuthResponse(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase(Locale.ROOT);
        if (loginAttemptService.isBlocked(email)) {
            throw new UnauthorizedException("Account is temporarily locked due to too many failed attempts. Try again later.");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User user = principal.getUser();
            requireVerifiedLogin(user);
            loginAttemptService.reset(email);
            log.info("User logged in: {}", email);
            return buildAuthResponse(user);
        } catch (AuthenticationException ex) {
            loginAttemptService.recordFailure(email);
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Rotation with reuse detection: if a refresh token that is no longer the
     * active session is presented, the whole session is revoked and the caller
     * must re-authenticate — this invalidates any token that leaked.</p>
     */
    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        try {
            String username = jwtService.extractUsername(request.getRefreshToken());
            if (!JwtService.TOKEN_TYPE_REFRESH.equals(jwtService.extractTokenType(request.getRefreshToken()))) {
                throw new BadRequestException("Provided token is not a refresh token");
            }
            String presentedJti = jwtService.extractJti(request.getRefreshToken());
            if (!tokenBlacklistService.isRefreshTokenValid(username, presentedJti)) {
                // Reuse of a rotated or revoked token: kill the session entirely.
                tokenBlacklistService.revokeRefreshToken(username);
                log.warn("Refresh token reuse detected for user: {}", username);
                throw new UnauthorizedException("Refresh token is invalid or has been revoked");
            }
            User user = userRepository.findByEmail(username)
                    .filter(User::isEnabled)
                    .orElseThrow(() -> new UnauthorizedException("Account is disabled or does not exist"));
            tokenBlacklistService.revokeRefreshToken(username);
            return buildAuthResponse(user);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(String accessToken) {
        if (StringUtils.hasText(accessToken)) {
            try {
                String username = jwtService.extractUsername(accessToken);
                tokenBlacklistService.revokeRefreshToken(username);
                tokenBlacklistService.blacklistAccessToken(accessToken);
                log.info("User logged out: {}", username);
            } catch (Exception ex) {
                log.warn("Logout could not fully revoke session: {}", ex.getMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        String email = request.getEmail().toLowerCase(Locale.ROOT);
        User user = findEnabledUser(email);
        verificationCodeService.verify(user.getId(), VerificationCodeType.EMAIL_VERIFICATION, request.getCode());
        user.setEmailVerified(true);
        userRepository.save(user);
        log.info("Email verified: {}", email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void resendVerification(ResendVerificationRequest request) {
        String email = request.getEmail().toLowerCase(Locale.ROOT);
        User user = findEnabledUser(email);
        if (user.isEmailVerified()) {
            throw new ConflictException("Email is already verified: " + email);
        }
        sendVerificationCode(user, VerificationCodeType.EMAIL_VERIFICATION);
        log.info("Re-issued email verification code for: {}", email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail().toLowerCase(Locale.ROOT);
        User user = findEnabledUser(email);
        sendVerificationCode(user, VerificationCodeType.PASSWORD_RESET);
        log.info("Password reset code issued for: {}", email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail().toLowerCase(Locale.ROOT);
        User user = findEnabledUser(email);
        verificationCodeService.verify(user.getId(), VerificationCodeType.PASSWORD_RESET, request.getCode());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
        verificationCodeService.invalidateActive(user.getId(), VerificationCodeType.PASSWORD_RESET);
        tokenBlacklistService.revokeRefreshToken(user.getEmail());
        log.info("Password reset completed for: {}", email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request, String accessToken) {
        UserPrincipal principal = currentPrincipal();
        User user = principal.getUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
        verificationCodeService.invalidateActive(user.getId(), VerificationCodeType.PASSWORD_RESET);
        if (StringUtils.hasText(accessToken)) {
            tokenBlacklistService.blacklistAccessToken(accessToken);
        }
        tokenBlacklistService.revokeRefreshToken(user.getEmail());
        log.info("Password changed for user: {}", user.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        return userMapper.toResponse(currentPrincipal().getUser());
    }

    /**
     * Issues a verification code for the user and delivers it by email.
     *
     * @param user the owning user
     * @param type the code purpose
     */
    private void sendVerificationCode(User user, VerificationCodeType type) {
        String code = verificationCodeService.issueCode(user, type);
        VerificationPurpose purpose = type == VerificationCodeType.EMAIL_VERIFICATION
                ? VerificationPurpose.EMAIL_VERIFICATION
                : VerificationPurpose.PASSWORD_RESET;
        emailService.sendVerificationCode(user.getEmail(), code, purpose);
    }

    /**
     * Blocks login for unverified accounts when the policy requires verification.
     *
     * @param user the authenticated user
     */
    private void requireVerifiedLogin(User user) {
        if (verificationProperties.isRequireVerifiedLogin() && !user.isEmailVerified()) {
            throw new EmailNotVerifiedException(
                    "Email not verified. Please verify your email before logging in: " + user.getEmail());
        }
    }

    /**
     * Issues an access token, a fresh refresh session and the user profile.
     *
     * @param user the authenticated user
     * @return the assembled auth response
     */
    private AuthResponse buildAuthResponse(User user) {
        UserPrincipal principal = UserPrincipal.create(user);
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        tokenBlacklistService.storeRefreshToken(user.getEmail(), jwtService.extractJti(refreshToken));
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInMs(jwtProperties.getAccessTokenExpirationMs())
                .user(userMapper.toResponse(user))
                .build();
    }

    /**
     * Resolves an enabled user by email.
     *
     * @param email the email address
     * @return the persisted user
     */
    private User findEnabledUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        if (!user.isEnabled()) {
            throw new ConflictException("Account is disabled: " + email);
        }
        return user;
    }

    /**
     * Resolves the currently authenticated principal.
     *
     * @return the user principal
     */
    private UserPrincipal currentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthenticationServiceException("No authenticated user found");
        }
        return principal;
    }
}
