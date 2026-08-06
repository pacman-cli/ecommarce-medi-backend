package com.example.ecommerce.audit.service.impl;

import com.example.ecommerce.audit.dto.enums.AuditAction;
import com.example.ecommerce.audit.dto.request.ActivityLogFilterRequest;
import com.example.ecommerce.audit.dto.request.AuditLogFilterRequest;
import com.example.ecommerce.audit.dto.request.CreateActivityLogRequest;
import com.example.ecommerce.audit.dto.request.LoginHistoryFilterRequest;
import com.example.ecommerce.audit.dto.response.ActivityLogResponse;
import com.example.ecommerce.audit.dto.response.AuditLogResponse;
import com.example.ecommerce.audit.dto.response.AuditSummaryResponse;
import com.example.ecommerce.audit.dto.response.LoginHistoryResponse;
import com.example.ecommerce.audit.entity.ActivityLog;
import com.example.ecommerce.audit.entity.AuditLog;
import com.example.ecommerce.audit.entity.AuditLogRepository;
import com.example.ecommerce.audit.entity.LoginHistory;
import com.example.ecommerce.audit.mapper.AuditMapper;
import com.example.ecommerce.audit.repository.ActivityLogRepository;
import com.example.ecommerce.audit.repository.LoginHistoryRepository;
import com.example.ecommerce.audit.service.AuditService;
import com.example.ecommerce.audit.specification.ActivityLogSpecification;
import com.example.ecommerce.audit.specification.AuditLogSpecification;
import com.example.ecommerce.audit.specification.LoginHistorySpecification;
import com.example.ecommerce.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Implementation of {@link AuditService} handling entity change auditing, activity logging,
 * authentication login history recording, and security metrics summaries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ActivityLogRepository activityLogRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final AuditMapper auditMapper;

    @Override
    public AuditLogResponse recordEntityAudit(String entityName, String entityId, AuditAction action,
                                             String oldState, String newState, Long userId,
                                             String username, String ipAddress, String userAgent, String deletedBy) {
        log.debug("Recording entity audit for {}:{} action: {}", entityName, entityId, action);
        AuditLog logEntry = AuditLog.builder()
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .oldState(oldState)
                .newState(newState)
                .userId(userId)
                .username(username)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .deletedBy(deletedBy)
                .timestamp(Instant.now())
                .build();

        AuditLog saved = auditLogRepository.save(logEntry);
        return auditMapper.toAuditLogResponse(saved);
    }

    @Override
    public ActivityLogResponse recordActivity(CreateActivityLogRequest request, Long userId,
                                               String username, String ipAddress, String userAgent) {
        log.debug("Recording activity type: {}, description: {}", request.getActivityType(), request.getDescription());
        ActivityLog logEntry = ActivityLog.builder()
                .activityType(request.getActivityType())
                .module(request.getModule())
                .description(request.getDescription())
                .metadata(request.getMetadata())
                .userId(userId)
                .username(username)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .isAdminActivity(Boolean.TRUE.equals(request.getIsAdminActivity()))
                .timestamp(Instant.now())
                .build();

        ActivityLog saved = activityLogRepository.save(logEntry);
        return auditMapper.toActivityLogResponse(saved);
    }

    @Override
    public LoginHistoryResponse recordLoginHistory(String userEmail, Long userId, boolean success,
                                                    String failureReason, String ipAddress, String userAgent, String location) {
        log.debug("Recording login history for email: {}, success: {}", userEmail, success);
        LoginHistory history = LoginHistory.builder()
                .userEmail(userEmail)
                .userId(userId)
                .success(success)
                .failureReason(failureReason)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .location(location)
                .timestamp(Instant.now())
                .build();

        LoginHistory saved = loginHistoryRepository.save(history);
        return auditMapper.toLoginHistoryResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> getAuditLogs(AuditLogFilterRequest filter, Pageable pageable) {
        Page<AuditLog> page = auditLogRepository.findAll(AuditLogSpecification.filterBy(filter), pageable);
        return PageResponse.from(page, auditMapper::toAuditLogResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ActivityLogResponse> getActivityLogs(ActivityLogFilterRequest filter, Pageable pageable) {
        Page<ActivityLog> page = activityLogRepository.findAll(ActivityLogSpecification.filterBy(filter), pageable);
        return PageResponse.from(page, auditMapper::toActivityLogResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ActivityLogResponse> getAdminActivities(Pageable pageable) {
        Page<ActivityLog> page = activityLogRepository.findByIsAdminActivityTrue(pageable);
        return PageResponse.from(page, auditMapper::toActivityLogResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<LoginHistoryResponse> getLoginHistories(LoginHistoryFilterRequest filter, Pageable pageable) {
        Page<LoginHistory> page = loginHistoryRepository.findAll(LoginHistorySpecification.filterBy(filter), pageable);
        return PageResponse.from(page, auditMapper::toLoginHistoryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditSummaryResponse getAuditSummary() {
        long totalAudit = auditLogRepository.count();
        long totalActivity = activityLogRepository.count();
        long totalAdminActivity = activityLogRepository.countByIsAdminActivityTrue();
        long totalLogin = loginHistoryRepository.count();
        long failedLogin = loginHistoryRepository.countBySuccessFalse();

        return AuditSummaryResponse.builder()
                .totalAuditLogs(totalAudit)
                .totalActivityLogs(totalActivity)
                .totalAdminActivities(totalAdminActivity)
                .totalLoginAttempts(totalLogin)
                .failedLoginAttempts(failedLogin)
                .build();
    }
}
