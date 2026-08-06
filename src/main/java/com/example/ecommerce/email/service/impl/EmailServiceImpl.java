package com.example.ecommerce.email.service.impl;

import com.example.ecommerce.email.dto.request.SendOrderConfirmationEmailRequest;
import com.example.ecommerce.email.dto.request.SendOrderStatusEmailRequest;
import com.example.ecommerce.email.dto.request.SendPasswordResetEmailRequest;
import com.example.ecommerce.email.dto.request.SendWelcomeEmailRequest;
import com.example.ecommerce.email.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

/**
 * Service implementation managing asynchronous email template compilation and JavaMailSender SMTP dispatches.
 */
@Slf4j
@Service("appEmailService")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username:noreply@example.com}")
    private String fromEmail;

    @Override
    @Async
    public void sendWelcomeEmail(SendWelcomeEmailRequest request) {
        log.info("Sending welcome email to: {}", request.getRecipientEmail());

        Context context = new Context();
        context.setVariable("userName", request.getUserName());
        context.setVariable("actionUrl", request.getActionUrl() != null ? request.getActionUrl() : "https://store.example.com");

        String htmlContent = templateEngine.process("welcome", context);
        sendHtmlEmail(request.getRecipientEmail(), "Welcome to E-Commerce Store!", htmlContent);
    }

    @Override
    @Async
    public void sendPasswordResetEmail(SendPasswordResetEmailRequest request) {
        log.info("Sending password reset email to: {}", request.getRecipientEmail());

        Context context = new Context();
        context.setVariable("userName", request.getUserName() != null ? request.getUserName() : "Customer");
        context.setVariable("resetOtp", request.getResetOtp());

        String htmlContent = templateEngine.process("forgot-password", context);
        sendHtmlEmail(request.getRecipientEmail(), "Password Reset Request OTP", htmlContent);
    }

    @Override
    @Async
    public void sendOrderConfirmationEmail(SendOrderConfirmationEmailRequest request) {
        log.info("Sending order confirmation email for PO/ORD #: {} to: {}", request.getOrderNumber(), request.getRecipientEmail());

        Context context = new Context();
        context.setVariable("customerName", request.getCustomerName() != null ? request.getCustomerName() : "Customer");
        context.setVariable("orderNumber", request.getOrderNumber());
        context.setVariable("totalAmount", request.getTotalAmount() != null ? "$" + request.getTotalAmount() : "$0.00");
        context.setVariable("shippingAddress", request.getShippingAddress() != null ? request.getShippingAddress() : "Standard Delivery Address");
        context.setVariable("orderItems", request.getOrderItems());

        String htmlContent = templateEngine.process("order-confirmation", context);
        sendHtmlEmail(request.getRecipientEmail(), "Order Confirmation #" + request.getOrderNumber(), htmlContent);
    }

    @Override
    @Async
    public void sendOrderStatusEmail(SendOrderStatusEmailRequest request) {
        log.info("Sending order status update email for ORD #: {} to: {}", request.getOrderNumber(), request.getRecipientEmail());

        Context context = new Context();
        context.setVariable("customerName", request.getCustomerName() != null ? request.getCustomerName() : "Customer");
        context.setVariable("orderNumber", request.getOrderNumber());
        context.setVariable("orderStatus", request.getOrderStatus());
        context.setVariable("statusMessage", request.getStatusMessage() != null ? request.getStatusMessage() : "Your order status has been updated.");
        context.setVariable("trackingNumber", request.getTrackingNumber());

        String htmlContent = templateEngine.process("order-status", context);
        sendHtmlEmail(request.getRecipientEmail(), "Order Status Update #" + request.getOrderNumber(), htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Successfully dispatched email to: {} with subject: '{}'", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to: {} with subject: '{}'. Error: {}", to, subject, e.getMessage(), e);
        }
    }
}
