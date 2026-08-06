package com.example.ecommerce.notification.dto.request;

import com.example.ecommerce.notification.entity.NotificationChannel;
import com.example.ecommerce.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * Payload for dispatching an ad-hoc or template-based notification message.
 */
@Schema(description = "Payload for sending a notification")
public class SendNotificationRequest {

    @Schema(description = "Target user ID (optional)", example = "1")
    private Long userId;

    @NotBlank(message = "Recipient is required")
    @Size(max = 200, message = "Recipient must not exceed 200 characters")
    @Schema(description = "Target recipient email address, mobile number or device token", example = "user@example.com")
    private String recipient;

    @NotNull(message = "Notification channel is required")
    @Schema(description = "Target channel (EMAIL, SMS, PUSH, IN_APP)", example = "EMAIL")
    private NotificationChannel channel;

    @NotNull(message = "Notification type is required")
    @Schema(description = "Notification classification type", example = "ORDER_CONFIRMATION")
    private NotificationType type;

    @Schema(description = "Template code if using predefined template", example = "ORDER_CONFIRMATION")
    private String templateCode;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Schema(description = "Notification title or subject", example = "Your Order Has Been Confirmed!")
    private String title;

    @Schema(description = "Direct notification body text (used if templateCode is absent)")
    private String content;

    @Schema(description = "Dynamic placeholder replacement parameters (e.g. { \"userName\": \"John\", \"orderNumber\": \"ORD-12345\" })")
    private Map<String, String> templateParams;

    public SendNotificationRequest() {
    }

    public SendNotificationRequest(Long userId, String recipient, NotificationChannel channel, NotificationType type, String templateCode, String title, String content, Map<String, String> templateParams) {
        this.userId = userId;
        this.recipient = recipient;
        this.channel = channel;
        this.type = type;
        this.templateCode = templateCode;
        this.title = title;
        this.content = content;
        this.templateParams = templateParams;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Map<String, String> getTemplateParams() { return templateParams; }
    public void setTemplateParams(Map<String, String> templateParams) { this.templateParams = templateParams; }

    public static SendNotificationRequestBuilder builder() { return new SendNotificationRequestBuilder(); }

    public static class SendNotificationRequestBuilder {
        private Long userId;
        private String recipient;
        private NotificationChannel channel;
        private NotificationType type;
        private String templateCode;
        private String title;
        private String content;
        private Map<String, String> templateParams;

        SendNotificationRequestBuilder() {}

        public SendNotificationRequestBuilder userId(Long userId) { this.userId = userId; return this; }
        public SendNotificationRequestBuilder recipient(String recipient) { this.recipient = recipient; return this; }
        public SendNotificationRequestBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public SendNotificationRequestBuilder type(NotificationType type) { this.type = type; return this; }
        public SendNotificationRequestBuilder templateCode(String templateCode) { this.templateCode = templateCode; return this; }
        public SendNotificationRequestBuilder title(String title) { this.title = title; return this; }
        public SendNotificationRequestBuilder content(String content) { this.content = content; return this; }
        public SendNotificationRequestBuilder templateParams(Map<String, String> templateParams) { this.templateParams = templateParams; return this; }

        public SendNotificationRequest build() {
            return new SendNotificationRequest(userId, recipient, channel, type, templateCode, title, content, templateParams);
        }
    }
}
