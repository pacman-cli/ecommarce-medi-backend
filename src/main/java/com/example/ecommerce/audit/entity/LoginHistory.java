package com.example.ecommerce.audit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Audit log entry for user authentication login attempts.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "login_histories",
        indexes = {
                @Index(name = "idx_login_history_email", columnList = "user_email"),
                @Index(name = "idx_login_history_user", columnList = "user_id"),
                @Index(name = "idx_login_history_timestamp", columnList = "timestamp")
        }
)
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "failure_reason", length = 250)
    private String failureReason;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 300)
    private String userAgent;

    @Column(length = 100)
    private String location;

    @Column(nullable = false)
    @Builder.Default
    private Instant timestamp = Instant.now();
}
