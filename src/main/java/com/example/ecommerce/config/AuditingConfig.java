package com.example.ecommerce.config;

import com.example.ecommerce.common.constant.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Enables JPA auditing so that {@code @CreatedDate}, {@code @LastModifiedDate},
 * {@code @CreatedBy} and {@code @LastModifiedBy} fields are populated automatically.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
public class AuditingConfig {

    /**
     * Resolves the current auditor from the security context, falling back to
     * {@code "system"} for unauthenticated or automated operations.
     *
     * @return the auditor aware bean
     */
    @Bean
    public AuditorAware<String> securityAuditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.of(AppConstants.SYSTEM_AUDITOR);
            }
            String name = authentication.getName();
            return Optional.ofNullable(name).filter(value -> !value.isBlank())
                    .or(() -> Optional.of(AppConstants.SYSTEM_AUDITOR));
        };
    }
}
