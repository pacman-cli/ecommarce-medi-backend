package com.example.ecommerce.notification.dto.response;

import com.example.ecommerce.notification.entity.NotificationChannel;
import com.example.ecommerce.notification.entity.NotificationStatus;
import com.example.ecommerce.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Master notification response DTO.
 */
@Schema(description = "Notification details response")
public class NotificationResponse {

    @Schema(description = "Notification ID", example = "100")
    private Long id;

    @Schema(description = "Target user ID (if set)", example = "1")
    private Long userId;

    @Schema(description = "Recipient address/phone/token", example = "user@example.com")
    private String recipient;

    @Schema(description = "Channel (EMAIL, SMS, PUSH, IN_APP)", example = "EMAIL")
    private NotificationChannel channel;

    @Schema(description = "Notification type", example = "ORDER_CONFIRMATION")
    private NotificationType type;

    @Schema(description = "Template code if used", example = "ORDER_CONFIRMATION")
    private String templateCode;

    @Schema(description = "Notification title/subject", example = "Your Order Has Been Confirmed!")
    private String title;

    @Schema(description = "Dispatched notification body text")
    private String content;

    @Schema(description = "Delivery status", example = "SENT")
    private NotificationStatus status;

    @Schema(description = "Timestamp when marked as read")
    private Instant readAt;

    @Schema(description = "Timestamp when sent by channel provider")
    private Instant sentAt;

    @Schema(description = "Retry attempts count", example = "0")
    private Integer retryCount;

    @Schema(description = "Failure error message if failed")
    private String errorMessage;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, Long userId, String recipient, NotificationChannel channel, NotificationType type, String templateCode, String title, String content, NotificationStatus status, Instant readAt, Instant sentAt, Integer retryCount, String errorMessage, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.recipient = recipient;
        this.channel = channel;
        this.type = type;
        this.templateCode = templateCode;
        this.title = title;
        this.content = content;
        this.status = status;
        this.readAt = readAt;
        this.sentAt = sentAt;
        this.retryCount = retryCount;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }

    public Instant getReadAt() { return readAt; }
    public void setReadAt(Instant readAt) { this.readAt = readAt; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static NotificationResponseBuilder builder() { return new NotificationResponseBuilder(); }

    public static class NotificationResponseBuilder {
        private Long id;
        private Long userId;
        private String recipient;
        private NotificationChannel channel;
        private NotificationType type;
        private String templateCode;
        private String title;
        private String content;
        private NotificationStatus status;
        private Instant readAt;
        private Instant sentAt;
        private Integer retryCount;
        private String errorMessage;
        private Instant createdAt;

        NotificationResponseBuilder() {}

        public NotificationResponseBuilder id(Long id) { this.id = id; return this; }
        public NotificationResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public NotificationResponseBuilder recipient(String recipient) { this.recipient = recipient; return this; }
        public NotificationResponseBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public NotificationResponseBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationResponseBuilder templateCode(String templateCode) { this.templateCode = templateCode; return this; }
        public NotificationResponseBuilder title(String title) { this.title = title; return this; }
        public NotificationResponseBuilder content(String content) { this.content = content; return this; }
        public NotificationResponseBuilder status(NotificationStatus status) { this.status = status; return this; }
        public NotificationResponseBuilder readAt(Instant readAt) { this.readAt = readAt; return this; }
        public NotificationResponseBuilder sentAt(Instant sentAt) { this.sentAt = sentAt; return this; }
        public NotificationResponseBuilder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public NotificationResponseBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public NotificationResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public NotificationResponse build() {
            return new NotificationResponse(id, userId, recipient, channel, type, templateCode, title, content, status, readAt, sentAt, retryCount, errorMessage, createdAt);
        }
    }
}
