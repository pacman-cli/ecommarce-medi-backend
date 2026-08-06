package com.example.ecommerce.notification.channel;

import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.entity.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SMS delivery channel strategy provider.
 */
@Component
@Slf4j
public class SmsNotificationChannel implements NotificationChannelStrategy {

    @Override
    public NotificationChannel getSupportedChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public boolean send(Notification notification) {
        log.info("Sending SMS notification to [{}]", notification.getRecipient());
        // Simulates Twilio / Local MNO SMS gateway delivery
        return true;
    }
}
