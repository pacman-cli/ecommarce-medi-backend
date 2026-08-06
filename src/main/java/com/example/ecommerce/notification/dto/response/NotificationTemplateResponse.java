package com.example.ecommerce.notification.dto.response;

import com.example.ecommerce.notification.entity.NotificationChannel;
import com.example.ecommerce.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Master notification template response DTO.
 */
@Schema(description = "Notification template details response")
public class NotificationTemplateResponse {

    @Schema(description = "Template ID", example = "5")
    private Long id;

    @Schema(description = "Unique template code identifier", example = "ORDER_CONFIRMATION")
    private String templateCode;

    @Schema(description = "Delivery channel", example = "EMAIL")
    private NotificationChannel channel;

    @Schema(description = "Notification type", example = "ORDER_CONFIRMATION")
    private NotificationType type;

    @Schema(description = "Subject line pattern", example = "Order {{orderNumber}} Confirmed")
    private String subject;

    @Schema(description = "Body template pattern")
    private String bodyTemplate;

    @Schema(description = "Active status flag", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    public NotificationTemplateResponse() {
    }

    public NotificationTemplateResponse(Long id, String templateCode, NotificationChannel channel, NotificationType type, String subject, String bodyTemplate, boolean active, Instant createdAt) {
        this.id = id;
        this.templateCode = templateCode;
        this.channel = channel;
        this.type = type;
        this.subject = subject;
        this.bodyTemplate = bodyTemplate;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static NotificationTemplateResponseBuilder builder() { return new NotificationTemplateResponseBuilder(); }

    public static class NotificationTemplateResponseBuilder {
        private Long id;
        private String templateCode;
        private NotificationChannel channel;
        private NotificationType type;
        private String subject;
        private String bodyTemplate;
        private boolean active;
        private Instant createdAt;

        NotificationTemplateResponseBuilder() {}

        public NotificationTemplateResponseBuilder id(Long id) { this.id = id; return this; }
        public NotificationTemplateResponseBuilder templateCode(String templateCode) { this.templateCode = templateCode; return this; }
        public NotificationTemplateResponseBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public NotificationTemplateResponseBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationTemplateResponseBuilder subject(String subject) { this.subject = subject; return this; }
        public NotificationTemplateResponseBuilder bodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; return this; }
        public NotificationTemplateResponseBuilder active(boolean active) { this.active = active; return this; }
        public NotificationTemplateResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public NotificationTemplateResponse build() {
            return new NotificationTemplateResponse(id, templateCode, channel, type, subject, bodyTemplate, active, createdAt);
        }
    }
}
