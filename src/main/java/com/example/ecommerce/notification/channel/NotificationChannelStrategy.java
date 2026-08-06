package com.example.ecommerce.notification.channel;

import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.entity.NotificationChannel;

/**
 * Strategy pattern interface for executing channel-specific delivery (Email, SMS, Push, In-App).
 */
public interface NotificationChannelStrategy {

    NotificationChannel getSupportedChannel();

    boolean send(Notification notification);
}
