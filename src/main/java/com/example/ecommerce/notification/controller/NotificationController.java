package com.example.ecommerce.notification.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.notification.dto.request.CreateTemplateRequest;
import com.example.ecommerce.notification.dto.request.NotificationFilterRequest;
import com.example.ecommerce.notification.dto.request.SendNotificationRequest;
import com.example.ecommerce.notification.dto.request.UpdateTemplateRequest;
import com.example.ecommerce.notification.dto.response.NotificationResponse;
import com.example.ecommerce.notification.dto.response.NotificationTemplateResponse;
import com.example.ecommerce.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing endpoints for multi-channel email, SMS, push and in-app notifications,
 * asynchronous Kafka event dispatching, template management and audit logs.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "Endpoints for Email, SMS, Push notifications, templates, async processing and delivery logs")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Send notification synchronously", description = "Dispatches a synchronous notification via selected channel (EMAIL, SMS, PUSH, IN_APP)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification dispatched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid channel or missing parameters")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        NotificationResponse response = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Notification dispatched successfully"));
    }

    @PostMapping("/send-async")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Send notification asynchronously", description = "Publishes a notification event for asynchronous processing (Kafka / Spring Event Publisher)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Notification event accepted for processing")
    })
    public ResponseEntity<ApiResponse<String>> sendNotificationAsync(
            @Valid @RequestBody SendNotificationRequest request) {
        notificationService.sendNotificationAsync(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success("Notification event published for asynchronous delivery", "Event accepted"));
    }

    @GetMapping("/my-notifications")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get user notifications", description = "Retrieves paginated notifications for the authenticated user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User notifications retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyNotifications(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<NotificationResponse> page = notificationService.getMyNotifications(pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "User notifications retrieved successfully"));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Mark notification as read", description = "Updates in-app notification status to READ with timestamp")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification marked as read")
    })
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @Parameter(description = "Notification ID", required = true) @PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Notification marked as read"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get notification log by ID", description = "Retrieves master notification audit record by ID (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification log retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(
            @Parameter(description = "Notification ID", required = true) @PathVariable Long id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Notification log retrieved successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get paginated notification logs", description = "Retrieves paginated master notification audit history with channel and status filters (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification logs retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getNotifications(
            @ModelAttribute NotificationFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<NotificationResponse> page = notificationService.getNotifications(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Notification logs retrieved successfully"));
    }

    @PostMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create notification template", description = "Creates a reusable message template with dynamic placeholders (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Template created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Duplicate template code")
    })
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> createTemplate(
            @Valid @RequestBody CreateTemplateRequest request) {
        NotificationTemplateResponse response = notificationService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Template created successfully"));
    }

    @PutMapping("/templates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update notification template", description = "Updates template pattern and subject line (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Template updated successfully")
    })
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> updateTemplate(
            @Parameter(description = "Template ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateTemplateRequest request) {
        NotificationTemplateResponse response = notificationService.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Template updated successfully"));
    }

    @GetMapping("/templates/code/{templateCode}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get template by code", description = "Retrieves notification template by unique template code (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Template retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> getTemplateByCode(
            @Parameter(description = "Template code", required = true) @PathVariable String templateCode) {
        NotificationTemplateResponse response = notificationService.getTemplateByCode(templateCode);
        return ResponseEntity.ok(ApiResponse.success(response, "Template retrieved successfully"));
    }

    @GetMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all notification templates", description = "Retrieves all notification templates (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Templates retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<NotificationTemplateResponse>>> getAllTemplates() {
        List<NotificationTemplateResponse> templates = notificationService.getAllTemplates();
        return ResponseEntity.ok(ApiResponse.success(templates, "Templates retrieved successfully"));
    }
}
