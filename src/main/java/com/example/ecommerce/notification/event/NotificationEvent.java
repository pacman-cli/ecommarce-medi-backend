package com.example.ecommerce.notification.event;

import com.example.ecommerce.notification.dto.request.SendNotificationRequest;

import java.io.Serializable;
import java.time.Instant;

/**
 * Event object representing an asynchronous notification request payload,
 * ready for Spring Event Listener processing or Apache Kafka topic publishing.
 */
public class NotificationEvent implements Serializable {

    private final String eventId;
    private final SendNotificationRequest notificationRequest;
    private final Instant timestamp;

    public NotificationEvent(String eventId, SendNotificationRequest notificationRequest) {
        this.eventId = eventId;
        this.notificationRequest = notificationRequest;
        this.timestamp = Instant.now();
    }

    public String getEventId() { return eventId; }
    public SendNotificationRequest getNotificationRequest() { return notificationRequest; }
    public Instant getTimestamp() { return timestamp; }
}
