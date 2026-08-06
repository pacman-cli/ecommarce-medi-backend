package com.example.ecommerce.audit.controller;

import com.example.ecommerce.audit.dto.request.ActivityLogFilterRequest;
import com.example.ecommerce.audit.dto.request.AuditLogFilterRequest;
import com.example.ecommerce.audit.dto.request.CreateActivityLogRequest;
import com.example.ecommerce.audit.dto.request.LoginHistoryFilterRequest;
import com.example.ecommerce.audit.dto.response.ActivityLogResponse;
import com.example.ecommerce.audit.dto.response.AuditLogResponse;
import com.example.ecommerce.audit.dto.response.AuditSummaryResponse;
import com.example.ecommerce.audit.dto.response.LoginHistoryResponse;
import com.example.ecommerce.audit.service.AuditService;
import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing system entity audit logs, activity event logs, authentication login history,
 * and security metrics.
 */
@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Audit & Security Logs", description = "Endpoints for inspecting entity change audits, user activities, admin actions, login attempts, and security logs")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/logs")
    @Operation(summary = "Search entity change audit logs (Paginated)", description = "Retrieves paginated list of entity mutation audit records matching search filter criteria")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<AuditLogResponse>>> getAuditLogs(
            AuditLogFilterRequest filter,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<AuditLogResponse> page = auditService.getAuditLogs(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Audit logs retrieved successfully"));
    }

    @GetMapping("/activities")
    @Operation(summary = "Search user and admin activity logs (Paginated)", description = "Retrieves paginated list of activity event logs matching search filter criteria")
    public ResponseEntity<ApiResponse<PageResponse<ActivityLogResponse>>> getActivityLogs(
            ActivityLogFilterRequest filter,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ActivityLogResponse> page = auditService.getActivityLogs(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Activity logs retrieved successfully"));
    }

    @PostMapping("/activities")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @Operation(summary = "Record activity event", description = "Programmatically logs a user or administrative activity event with client metadata")
    public ResponseEntity<ApiResponse<ActivityLogResponse>> recordActivity(
            @Valid @RequestBody CreateActivityLogRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletRequest httpRequest) {
        Long userId = userPrincipal != null ? userPrincipal.getUser().getId() : null;
        String username = userPrincipal != null ? userPrincipal.getUsername() : "SYSTEM";
        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        ActivityLogResponse response = auditService.recordActivity(request, userId, username, ipAddress, userAgent);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Activity log recorded successfully"));
    }

    @GetMapping("/admin-activities")
    @Operation(summary = "Search admin security activity logs (Paginated)", description = "Retrieves paginated list of security-sensitive administrative activity logs")
    public ResponseEntity<ApiResponse<PageResponse<ActivityLogResponse>>> getAdminActivities(
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ActivityLogResponse> page = auditService.getAdminActivities(pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Admin activity logs retrieved successfully"));
    }

    @GetMapping("/login-history")
    @Operation(summary = "Search login authentication histories (Paginated)", description = "Retrieves paginated list of successful and failed authentication login attempts")
    public ResponseEntity<ApiResponse<PageResponse<LoginHistoryResponse>>> getLoginHistories(
            LoginHistoryFilterRequest filter,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<LoginHistoryResponse> page = auditService.getLoginHistories(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Login history logs retrieved successfully"));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get system audit metrics summary", description = "Retrieves aggregate counts of entity audit logs, activity events, admin actions, and failed login attempts")
    public ResponseEntity<ApiResponse<AuditSummaryResponse>> getAuditSummary() {
        AuditSummaryResponse response = auditService.getAuditSummary();
        return ResponseEntity.ok(ApiResponse.success(response, "Audit summary metrics retrieved successfully"));
    }
}
