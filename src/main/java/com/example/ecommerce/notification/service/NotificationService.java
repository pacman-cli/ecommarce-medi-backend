package com.example.ecommerce.notification.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.notification.dto.request.CreateTemplateRequest;
import com.example.ecommerce.notification.dto.request.NotificationFilterRequest;
import com.example.ecommerce.notification.dto.request.SendNotificationRequest;
import com.example.ecommerce.notification.dto.request.UpdateTemplateRequest;
import com.example.ecommerce.notification.dto.response.NotificationResponse;
import com.example.ecommerce.notification.dto.response.NotificationTemplateResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for multi-channel notification dispatch, template engine interpolation,
 * Spring `@Async` processing, event publishing and history querying.
 */
public interface NotificationService {

    NotificationResponse sendNotification(SendNotificationRequest request);

    void sendNotificationAsync(SendNotificationRequest request);

    NotificationTemplateResponse createTemplate(CreateTemplateRequest request);

    NotificationTemplateResponse updateTemplate(Long id, UpdateTemplateRequest request);

    NotificationTemplateResponse getTemplateByCode(String templateCode);

    List<NotificationTemplateResponse> getAllTemplates();

    NotificationResponse getNotificationById(Long id);

    PageResponse<NotificationResponse> getMyNotifications(Pageable pageable);

    PageResponse<NotificationResponse> getNotifications(NotificationFilterRequest filter, Pageable pageable);

    void markAsRead(Long id);
}
