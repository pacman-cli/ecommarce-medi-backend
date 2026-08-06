package com.example.ecommerce.notification.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

/**
 * Audit log and delivery history entity for multi-channel email, SMS, push and in-app notifications.
 */
@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 200)
    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(name = "template_code", length = 50)
    private String templateCode;

    @Column(length = 200)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Notification() {
    }

    public void markSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void markFailed(String error) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = error;
        this.retryCount = (this.retryCount != null ? this.retryCount : 0) + 1;
    }

    public void markRead() {
        this.status = NotificationStatus.READ;
        this.readAt = Instant.now();
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static NotificationBuilder builder() { return new NotificationBuilder(); }

    public static class NotificationBuilder {
        private User user;
        private String recipient;
        private NotificationChannel channel;
        private NotificationType type;
        private String templateCode;
        private String title;
        private String content;
        private NotificationStatus status = NotificationStatus.PENDING;
        private Instant readAt;
        private Instant sentAt;
        private Integer retryCount = 0;
        private String errorMessage;
        private boolean deleted = false;
        private Instant deletedAt;

        NotificationBuilder() {}

        public NotificationBuilder user(User user) { this.user = user; return this; }
        public NotificationBuilder recipient(String recipient) { this.recipient = recipient; return this; }
        public NotificationBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public NotificationBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationBuilder templateCode(String templateCode) { this.templateCode = templateCode; return this; }
        public NotificationBuilder title(String title) { this.title = title; return this; }
        public NotificationBuilder content(String content) { this.content = content; return this; }
        public NotificationBuilder status(NotificationStatus status) { this.status = status; return this; }
        public NotificationBuilder readAt(Instant readAt) { this.readAt = readAt; return this; }
        public NotificationBuilder sentAt(Instant sentAt) { this.sentAt = sentAt; return this; }
        public NotificationBuilder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public NotificationBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public NotificationBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public NotificationBuilder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }

        public Notification build() {
            Notification n = new Notification();
            n.setUser(user);
            n.setRecipient(recipient);
            n.setChannel(channel);
            n.setType(type);
            n.setTemplateCode(templateCode);
            n.setTitle(title);
            n.setContent(content);
            n.setStatus(status != null ? status : NotificationStatus.PENDING);
            n.setReadAt(readAt);
            n.setSentAt(sentAt);
            n.setRetryCount(retryCount != null ? retryCount : 0);
            n.setErrorMessage(errorMessage);
            n.setDeleted(deleted);
            n.setDeletedAt(deletedAt);
            return n;
        }
    }
}
