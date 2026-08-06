package com.example.ecommerce.audit.service;

import com.example.ecommerce.audit.dto.enums.ActivityType;
import com.example.ecommerce.audit.dto.enums.AuditAction;
import com.example.ecommerce.audit.dto.request.CreateActivityLogRequest;
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
import com.example.ecommerce.audit.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private LoginHistoryRepository loginHistoryRepository;

    @Mock
    private AuditMapper auditMapper;

    @InjectMocks
    private AuditServiceImpl auditService;

    private AuditLog sampleAuditLog;
    private AuditLogResponse sampleAuditResponse;
    private ActivityLog sampleActivityLog;
    private ActivityLogResponse sampleActivityResponse;
    private LoginHistory sampleLoginHistory;
    private LoginHistoryResponse sampleLoginResponse;

    @BeforeEach
    void setUp() {
        sampleAuditLog = AuditLog.builder()
                .entityName("Product")
                .entityId("200")
                .action(AuditAction.UPDATE)
                .username("admin")
                .build();
        sampleAuditLog.setId(1001L);

        sampleAuditResponse = AuditLogResponse.builder()
                .id(1001L)
                .entityName("Product")
                .entityId("200")
                .action(AuditAction.UPDATE)
                .username("admin")
                .build();

        sampleActivityLog = ActivityLog.builder()
                .activityType(ActivityType.ADMIN_ACTION)
                .module("INVENTORY")
                .description("Stock adjusted")
                .isAdminActivity(true)
                .build();
        sampleActivityLog.setId(501L);

        sampleActivityResponse = ActivityLogResponse.builder()
                .id(501L)
                .activityType(ActivityType.ADMIN_ACTION)
                .module("INVENTORY")
                .description("Stock adjusted")
                .isAdminActivity(true)
                .build();

        sampleLoginHistory = LoginHistory.builder()
                .userEmail("john.doe@example.com")
                .success(true)
                .ipAddress("192.168.1.1")
                .build();
        sampleLoginHistory.setId(201L);

        sampleLoginResponse = LoginHistoryResponse.builder()
                .id(201L)
                .userEmail("john.doe@example.com")
                .success(true)
                .ipAddress("192.168.1.1")
                .build();
    }

    @Test
    void testRecordEntityAuditSuccess() {
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(sampleAuditLog);
        when(auditMapper.toAuditLogResponse(any(AuditLog.class))).thenReturn(sampleAuditResponse);

        AuditLogResponse response = auditService.recordEntityAudit(
                "Product", "200", AuditAction.UPDATE, "{}", "{}", 1L, "admin", "127.0.0.1", "Mozilla", null
        );

        assertNotNull(response);
        assertEquals(1001L, response.getId());
        assertEquals("Product", response.getEntityName());
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void testRecordActivitySuccess() {
        when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(sampleActivityLog);
        when(auditMapper.toActivityLogResponse(any(ActivityLog.class))).thenReturn(sampleActivityResponse);

        CreateActivityLogRequest request = CreateActivityLogRequest.builder()
                .activityType(ActivityType.ADMIN_ACTION)
                .module("INVENTORY")
                .description("Stock adjusted")
                .isAdminActivity(true)
                .build();

        ActivityLogResponse response = auditService.recordActivity(request, 1L, "admin", "127.0.0.1", "Mozilla");

        assertNotNull(response);
        assertEquals(501L, response.getId());
        assertTrue(response.isAdminActivity());
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
    }

    @Test
    void testRecordLoginHistorySuccess() {
        when(loginHistoryRepository.save(any(LoginHistory.class))).thenReturn(sampleLoginHistory);
        when(auditMapper.toLoginHistoryResponse(any(LoginHistory.class))).thenReturn(sampleLoginResponse);

        LoginHistoryResponse response = auditService.recordLoginHistory(
                "john.doe@example.com", 5L, true, null, "192.168.1.1", "Mozilla", "Dhaka"
        );

        assertNotNull(response);
        assertEquals(201L, response.getId());
        assertTrue(response.isSuccess());
        verify(loginHistoryRepository, times(1)).save(any(LoginHistory.class));
    }

    @Test
    void testGetAuditSummary() {
        when(auditLogRepository.count()).thenReturn(100L);
        when(activityLogRepository.count()).thenReturn(200L);
        when(activityLogRepository.countByIsAdminActivityTrue()).thenReturn(50L);
        when(loginHistoryRepository.count()).thenReturn(500L);
        when(loginHistoryRepository.countBySuccessFalse()).thenReturn(10L);

        AuditSummaryResponse response = auditService.getAuditSummary();

        assertNotNull(response);
        assertEquals(100L, response.getTotalAuditLogs());
        assertEquals(200L, response.getTotalActivityLogs());
        assertEquals(50L, response.getTotalAdminActivities());
        assertEquals(500L, response.getTotalLoginAttempts());
        assertEquals(10L, response.getFailedLoginAttempts());
    }
}
