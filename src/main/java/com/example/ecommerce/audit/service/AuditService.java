package com.example.ecommerce.audit.service;

import com.example.ecommerce.audit.dto.enums.AuditAction;
import com.example.ecommerce.audit.dto.request.ActivityLogFilterRequest;
import com.example.ecommerce.audit.dto.request.AuditLogFilterRequest;
import com.example.ecommerce.audit.dto.request.CreateActivityLogRequest;
import com.example.ecommerce.audit.dto.request.LoginHistoryFilterRequest;
import com.example.ecommerce.audit.dto.response.ActivityLogResponse;
import com.example.ecommerce.audit.dto.response.AuditLogResponse;
import com.example.ecommerce.audit.dto.response.AuditSummaryResponse;
import com.example.ecommerce.audit.dto.response.LoginHistoryResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * Service interface defining business operations for recording entity change audits,
 * user activity events, login history attempts, admin activity tracking, and security summaries.
 */
public interface AuditService {

    /**
     * Programmatically records an entity mutation change audit entry.
     */
    AuditLogResponse recordEntityAudit(String entityName, String entityId, AuditAction action, String oldState, String newState, Long userId, String username, String ipAddress, String userAgent, String deletedBy);

    /**
     * Programmatically records a user or administrative activity event.
     */
    ActivityLogResponse recordActivity(CreateActivityLogRequest request, Long userId, String username, String ipAddress, String userAgent);

    /**
     * Programmatically records an authentication login attempt history entry.
     */
    LoginHistoryResponse recordLoginHistory(String userEmail, Long userId, boolean success, String failureReason, String ipAddress, String userAgent, String location);

    /**
     * Retrieves paginated list of entity change audit logs matching specification filter criteria.
     */
    PageResponse<AuditLogResponse> getAuditLogs(AuditLogFilterRequest filter, Pageable pageable);

    /**
     * Retrieves paginated list of activity logs matching specification filter criteria.
     */
    PageResponse<ActivityLogResponse> getActivityLogs(ActivityLogFilterRequest filter, Pageable pageable);

    /**
     * Retrieves paginated list of administrative security activity logs.
     */
    PageResponse<ActivityLogResponse> getAdminActivities(Pageable pageable);

    /**
     * Retrieves paginated list of login histories matching specification filter criteria.
     */
    PageResponse<LoginHistoryResponse> getLoginHistories(LoginHistoryFilterRequest filter, Pageable pageable);

    /**
     * Computes cumulative system audit and security logging summary metrics.
     */
    AuditSummaryResponse getAuditSummary();
}
