package com.example.ecommerce.email.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.email.dto.request.SendOrderConfirmationEmailRequest;
import com.example.ecommerce.email.dto.request.SendOrderStatusEmailRequest;
import com.example.ecommerce.email.dto.request.SendPasswordResetEmailRequest;
import com.example.ecommerce.email.dto.request.SendWelcomeEmailRequest;
import com.example.ecommerce.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for testing and dispatching email notifications.
 */
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Email Notifications", description = "Endpoints for testing and dispatching HTML email notifications")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/welcome")
    @Operation(summary = "Send welcome onboarding email", description = "Dispatches welcome HTML email to specified recipient")
    public ResponseEntity<ApiResponse<Void>> sendWelcomeEmail(@Valid @RequestBody SendWelcomeEmailRequest request) {
        emailService.sendWelcomeEmail(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Welcome email queued for dispatch"));
    }

    @PostMapping("/password-reset")
    @Operation(summary = "Send password reset OTP email", description = "Dispatches password reset OTP HTML email to specified recipient")
    public ResponseEntity<ApiResponse<Void>> sendPasswordResetEmail(@Valid @RequestBody SendPasswordResetEmailRequest request) {
        emailService.sendPasswordResetEmail(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset email queued for dispatch"));
    }

    @PostMapping("/order-confirmation")
    @Operation(summary = "Send order confirmation invoice email", description = "Dispatches order purchase confirmation invoice HTML email")
    public ResponseEntity<ApiResponse<Void>> sendOrderConfirmationEmail(@Valid @RequestBody SendOrderConfirmationEmailRequest request) {
        emailService.sendOrderConfirmationEmail(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Order confirmation email queued for dispatch"));
    }

    @PostMapping("/order-status")
    @Operation(summary = "Send order status update email", description = "Dispatches order status update HTML email")
    public ResponseEntity<ApiResponse<Void>> sendOrderStatusEmail(@Valid @RequestBody SendOrderStatusEmailRequest request) {
        emailService.sendOrderStatusEmail(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Order status update email queued for dispatch"));
    }
}
