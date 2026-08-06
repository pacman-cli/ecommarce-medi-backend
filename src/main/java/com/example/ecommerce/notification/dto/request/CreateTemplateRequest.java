package com.example.ecommerce.notification.dto.request;

import com.example.ecommerce.notification.entity.NotificationChannel;
import com.example.ecommerce.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating a notification template.
 */
@Schema(description = "Payload for creating a notification template")
public class CreateTemplateRequest {

    @NotBlank(message = "Template code is required")
    @Size(max = 50, message = "Template code must not exceed 50 characters")
    @Schema(description = "Unique template code identifier", example = "ORDER_CONFIRMATION")
    private String templateCode;

    @NotNull(message = "Channel is required")
    @Schema(description = "Channel for template", example = "EMAIL")
    private NotificationChannel channel;

    @NotNull(message = "Notification type is required")
    @Schema(description = "Notification classification type", example = "ORDER_CONFIRMATION")
    private NotificationType type;

    @Size(max = 200, message = "Subject must not exceed 200 characters")
    @Schema(description = "Subject line pattern", example = "Order {{orderNumber}} Confirmed")
    private String subject;

    @NotBlank(message = "Body template is required")
    @Schema(description = "Body template string with placeholder variables", example = "Hello {{userName}}, your order {{orderNumber}} for ${{totalAmount}} is placed!")
    private String bodyTemplate;

    public CreateTemplateRequest() {
    }

    public CreateTemplateRequest(String templateCode, NotificationChannel channel, NotificationType type, String subject, String bodyTemplate) {
        this.templateCode = templateCode;
        this.channel = channel;
        this.type = type;
        this.subject = subject;
        this.bodyTemplate = bodyTemplate;
    }

    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }

    public static CreateTemplateRequestBuilder builder() { return new CreateTemplateRequestBuilder(); }

    public static class CreateTemplateRequestBuilder {
        private String templateCode;
        private NotificationChannel channel;
        private NotificationType type;
        private String subject;
        private String bodyTemplate;

        CreateTemplateRequestBuilder() {}

        public CreateTemplateRequestBuilder templateCode(String templateCode) { this.templateCode = templateCode; return this; }
        public CreateTemplateRequestBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public CreateTemplateRequestBuilder type(NotificationType type) { this.type = type; return this; }
        public CreateTemplateRequestBuilder subject(String subject) { this.subject = subject; return this; }
        public CreateTemplateRequestBuilder bodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; return this; }

        public CreateTemplateRequest build() {
            return new CreateTemplateRequest(templateCode, channel, type, subject, bodyTemplate);
        }
    }
}
