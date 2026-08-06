package com.example.ecommerce.audit.entity;

import com.example.ecommerce.audit.dto.enums.ActivityType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * User and administrative activity event log.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "activity_logs",
        indexes = {
                @Index(name = "idx_activity_log_user", columnList = "user_id"),
                @Index(name = "idx_activity_log_type", columnList = "activity_type"),
                @Index(name = "idx_activity_log_admin", columnList = "is_admin_activity")
        }
)
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 40)
    private ActivityType activityType;

    @Column(length = 60)
    private String module;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100)
    private String username;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 300)
    private String userAgent;

    @Column(name = "is_admin_activity", nullable = false)
    @Builder.Default
    private boolean isAdminActivity = false;

    @Column(nullable = false)
    @Builder.Default
    private Instant timestamp = Instant.now();
}
