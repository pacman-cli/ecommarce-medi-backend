package com.example.ecommerce.notification.channel;

import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.entity.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mobile and Web Push notification channel strategy provider.
 */
@Component
@Slf4j
public class PushNotificationChannel implements NotificationChannelStrategy {

    @Override
    public NotificationChannel getSupportedChannel() {
        return NotificationChannel.PUSH;
    }

    @Override
    public boolean send(Notification notification) {
        log.info("Sending PUSH notification to device token [{}] with title: '{}'", notification.getRecipient(), notification.getTitle());
        // Simulates FCM (Firebase Cloud Messaging) / APNS delivery
        return true;
    }
}
