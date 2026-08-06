package com.example.ecommerce.notification.channel;

import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.entity.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Email delivery channel strategy provider.
 */
@Component
@Slf4j
public class EmailNotificationChannel implements NotificationChannelStrategy {

    @Override
    public NotificationChannel getSupportedChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public boolean send(Notification notification) {
        log.info("Sending EMAIL notification to [{}] with subject: '{}'", notification.getRecipient(), notification.getTitle());
        // Simulates SMTP / JavaMail / SendGrid delivery
        return true;
    }
}
