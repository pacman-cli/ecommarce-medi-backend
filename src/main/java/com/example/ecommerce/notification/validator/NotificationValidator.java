package com.example.ecommerce.notification.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.notification.dto.request.SendNotificationRequest;
import com.example.ecommerce.notification.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Validates notification requests and template uniqueness.
 */
@Component
@RequiredArgsConstructor
public class NotificationValidator {

    private final NotificationTemplateRepository templateRepository;

    public void validateSendRequest(SendNotificationRequest request) {
        if (request == null) {
            throw new BadRequestException("Notification request cannot be null");
        }
        if (!StringUtils.hasText(request.getRecipient())) {
            throw new BadRequestException("Recipient address, phone number or device token is required");
        }
        if (!StringUtils.hasText(request.getTemplateCode()) && !StringUtils.hasText(request.getContent())) {
            throw new BadRequestException("Either templateCode or direct message content must be provided");
        }
    }

    public void validateTemplateCodeUnique(String templateCode) {
        if (templateRepository.existsByTemplateCodeAndDeletedFalse(templateCode)) {
            throw new BadRequestException("Notification template already exists with code: " + templateCode);
        }
    }
}
