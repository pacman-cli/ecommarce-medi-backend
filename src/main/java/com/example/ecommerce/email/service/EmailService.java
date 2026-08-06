package com.example.ecommerce.email.service;

import com.example.ecommerce.email.dto.request.SendOrderConfirmationEmailRequest;
import com.example.ecommerce.email.dto.request.SendOrderStatusEmailRequest;
import com.example.ecommerce.email.dto.request.SendPasswordResetEmailRequest;
import com.example.ecommerce.email.dto.request.SendWelcomeEmailRequest;

/**
 * Service interface defining asynchronous operations for compiling Thymeleaf HTML email templates
 * and dispatching SMTP notifications.
 */
public interface EmailService {

    /**
     * Sends welcome onboarding HTML email.
     */
    void sendWelcomeEmail(SendWelcomeEmailRequest request);

    /**
     * Sends password reset OTP / verification HTML email.
     */
    void sendPasswordResetEmail(SendPasswordResetEmailRequest request);

    /**
     * Sends order purchase invoice confirmation HTML email.
     */
    void sendOrderConfirmationEmail(SendOrderConfirmationEmailRequest request);

    /**
     * Sends order status update notification HTML email.
     */
    void sendOrderStatusEmail(SendOrderStatusEmailRequest request);
}
