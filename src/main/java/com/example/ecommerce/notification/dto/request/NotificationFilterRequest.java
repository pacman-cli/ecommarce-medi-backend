package com.example.ecommerce.notification.dto.request;

import com.example.ecommerce.notification.entity.NotificationChannel;
import com.example.ecommerce.notification.entity.NotificationStatus;
import com.example.ecommerce.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Filter parameters for searching notification logs and user history.
 */
@Schema(description = "Notification search and filter criteria")
public class NotificationFilterRequest {

    @Schema(description = "Target user ID filter", example = "1")
    private Long userId;

    @Schema(description = "Delivery channel filter", example = "EMAIL")
    private NotificationChannel channel;

    @Schema(description = "Notification type filter", example = "ORDER_CONFIRMATION")
    private NotificationType type;

    @Schema(description = "Status filter (PENDING, SENT, FAILED, READ)", example = "SENT")
    private NotificationStatus status;

    @Schema(description = "Keyword search matching recipient, title or content", example = "john@example.com")
    private String search;

    public NotificationFilterRequest() {
    }

    public NotificationFilterRequest(Long userId, NotificationChannel channel, NotificationType type, NotificationStatus status, String search) {
        this.userId = userId;
        this.channel = channel;
        this.type = type;
        this.status = status;
        this.search = search;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public static NotificationFilterRequestBuilder builder() { return new NotificationFilterRequestBuilder(); }

    public static class NotificationFilterRequestBuilder {
        private Long userId;
        private NotificationChannel channel;
        private NotificationType type;
        private NotificationStatus status;
        private String search;

        NotificationFilterRequestBuilder() {}

        public NotificationFilterRequestBuilder userId(Long userId) { this.userId = userId; return this; }
        public NotificationFilterRequestBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public NotificationFilterRequestBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationFilterRequestBuilder status(NotificationStatus status) { this.status = status; return this; }
        public NotificationFilterRequestBuilder search(String search) { this.search = search; return this; }

        public NotificationFilterRequest build() {
            return new NotificationFilterRequest(userId, channel, type, status, search);
        }
    }
}
