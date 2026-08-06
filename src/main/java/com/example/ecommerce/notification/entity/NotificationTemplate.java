package com.example.ecommerce.notification.entity;

import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

/**
 * Reusable notification message template with placeholder support (e.g. {{userName}}, {{orderNumber}}).
 */
@Entity
@Table(
        name = "notification_templates",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_notification_templates_code", columnNames = "template_code")
        }
)
@SQLDelete(sql = "UPDATE notification_templates SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class NotificationTemplate extends BaseEntity {

    @Column(name = "template_code", nullable = false, length = 50)
    private String templateCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(length = 200)
    private String subject;

    @Lob
    @Column(name = "body_template", columnDefinition = "TEXT", nullable = false)
    private String bodyTemplate;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public NotificationTemplate() {
    }

    public NotificationTemplate(String templateCode, NotificationChannel channel, NotificationType type, String subject, String bodyTemplate, boolean active) {
        this.templateCode = templateCode;
        this.channel = channel;
        this.type = type;
        this.subject = subject;
        this.bodyTemplate = bodyTemplate;
        this.active = active;
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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static NotificationTemplateBuilder builder() { return new NotificationTemplateBuilder(); }

    public static class NotificationTemplateBuilder {
        private String templateCode;
        private NotificationChannel channel;
        private NotificationType type;
        private String subject;
        private String bodyTemplate;
        private boolean active = true;

        NotificationTemplateBuilder() {}

        public NotificationTemplateBuilder templateCode(String templateCode) { this.templateCode = templateCode; return this; }
        public NotificationTemplateBuilder channel(NotificationChannel channel) { this.channel = channel; return this; }
        public NotificationTemplateBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationTemplateBuilder subject(String subject) { this.subject = subject; return this; }
        public NotificationTemplateBuilder bodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; return this; }
        public NotificationTemplateBuilder active(boolean active) { this.active = active; return this; }

        public NotificationTemplate build() {
            return new NotificationTemplate(templateCode, channel, type, subject, bodyTemplate, active);
        }
    }
}
