package com.example.ecommerce.notification.event;

import com.example.ecommerce.notification.dto.request.SendNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Event publisher bridge that dispatches notification events asynchronously.
 * Designed to seamlessly bridge with Apache Kafka topics ('notification-events-topic') or Spring Event Listeners.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishNotificationEvent(SendNotificationRequest request) {
        String eventId = UUID.randomUUID().toString();
        NotificationEvent event = new NotificationEvent(eventId, request);

        log.info("Publishing notification event ID {} for channel {} and type {}", eventId, request.getChannel(), request.getType());

        // Spring ApplicationEvent (can easily delegate to kafkaTemplate.send("notification-topic", event) in production)
        applicationEventPublisher.publishEvent(event);
    }
}
