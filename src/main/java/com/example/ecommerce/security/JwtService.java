package com.example.ecommerce.security;

import com.example.ecommerce.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Generates and parses signed JWT access and refresh tokens.
 *
 * <p>Tokens are signed with HS256. The refresh token additionally carries a
 * {@code jti} claim that ties it to the Redis-backed session managed by
 * {@link TokenBlacklistService}.</p>
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_TOKEN_TYPE = "tokenType";
    public static final String CLAIM_JTI = "jti";
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    private final JwtProperties jwtProperties;

    /**
     * Generates an access token for the given principal.
     *
     * @param principal the authenticated user
     * @return the signed access token
     */
    public String generateAccessToken(UserPrincipal principal) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(principal.getUsername())
                .claim(CLAIM_USER_ID, principal.getUser().getId())
                .claim(CLAIM_ROLE, principal.getUser().getRole().name())
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtProperties.getAccessTokenExpirationMs())))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generates a refresh token bound to a {@code jti} session identifier.
     *
     * @param username the owner of the token
     * @return the signed refresh token
     */
    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
                .claim(CLAIM_JTI, UUID.randomUUID().toString())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtProperties.getRefreshTokenExpirationMs())))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the subject (username) from a token.
     *
     * @param token the JWT
     * @return the subject
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts the user id claim from a token.
     *
     * @param token the JWT
     * @return the user id
     */
    public Long extractUserId(String token) {
        return extractClaims(token).get(CLAIM_USER_ID, Long.class);
    }

    /**
     * Extracts the session id claim from a refresh token.
     *
     * @param token the JWT
     * @return the jti
     */
    public String extractJti(String token) {
        return extractClaims(token).get(CLAIM_JTI, String.class);
    }

    /**
     * Extracts the token-type claim.
     *
     * @param token the JWT
     * @return the token type
     */
    public String extractTokenType(String token) {
        return extractClaims(token).get(CLAIM_TOKEN_TYPE, String.class);
    }

    /**
     * Validates a token against a user and its own expiration time.
     *
     * @param token       the JWT
     * @param userDetails the expected user
     * @return {@code true} when the subject matches and the token is not expired
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    /**
     * Returns the token expiration date.
     *
     * @param token the JWT
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
