package com.example.ecommerce.audit.entity;

import com.example.ecommerce.audit.dto.enums.AuditAction;
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
 * Entity change audit log entry recording entity mutations, old/new state JSON snapshots,
 * auditor username, IP address, and timestamps.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "audit_logs",
        indexes = {
                @Index(name = "idx_audit_log_entity", columnList = "entity_name, entity_id"),
                @Index(name = "idx_audit_log_user", columnList = "user_id"),
                @Index(name = "idx_audit_log_timestamp", columnList = "timestamp")
        }
)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name", nullable = false, length = 100)
    private String entityName;

    @Column(name = "entity_id", nullable = false, length = 100)
    private String entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditAction action;

    @Column(name = "old_state", columnDefinition = "TEXT")
    private String oldState;

    @Column(name = "new_state", columnDefinition = "TEXT")
    private String newState;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100)
    private String username;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 300)
    private String userAgent;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Column(nullable = false)
    @Builder.Default
    private Instant timestamp = Instant.now();
}
