package com.example.ecommerce.payment.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.payment.dto.request.InitiatePaymentRequest;
import com.example.ecommerce.payment.dto.request.PaymentFilterRequest;
import com.example.ecommerce.payment.dto.request.RefundPaymentRequest;
import com.example.ecommerce.payment.dto.request.VerifyPaymentRequest;
import com.example.ecommerce.payment.dto.response.PaymentInitiationResponse;
import com.example.ecommerce.payment.dto.response.PaymentResponse;
import com.example.ecommerce.payment.dto.response.PaymentVerificationResponse;
import com.example.ecommerce.payment.dto.response.RefundResponse;
import com.example.ecommerce.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller exposing endpoints for online payment initiation (SSLCommerz, bKash, Nagad, COD),
 * verification callbacks, IPN webhooks and refund processing.
 */
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Endpoints for multi-gateway payment initiation, IPN webhooks, verification and refunds")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @Operation(summary = "Initiate payment", description = "Initiates an online payment session (SSLCommerz, bKash, Nagad) or sets up Cash on Delivery (COD)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Payment session initiated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Order already paid or invalid gateway")
    })
    public ResponseEntity<ApiResponse<PaymentInitiationResponse>> initiatePayment(
            @Valid @RequestBody InitiatePaymentRequest request) {
        PaymentInitiationResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Payment initiated successfully"));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify payment", description = "Verifies payment transaction with gateway using callback query params or validation IDs")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment verification completed")
    })
    public ResponseEntity<ApiResponse<PaymentVerificationResponse>> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request,
            @RequestParam(required = false) Map<String, String> queryParams) {
        PaymentVerificationResponse response = paymentService.verifyPayment(request, queryParams);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment verification completed"));
    }

    @PostMapping("/webhook/{gateway}")
    @Operation(summary = "IPN Webhook callback", description = "Asynchronous IPN webhook listener for gateway callbacks (SSLCommerz, bKash, Nagad)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Webhook payload processed successfully")
    })
    public ResponseEntity<ApiResponse<PaymentVerificationResponse>> processWebhook(
            @Parameter(description = "Gateway identifier (e.g. sslcommerz, bkash, nagad)", required = true) @PathVariable String gateway,
            @RequestParam Map<String, String> payload) {
        PaymentVerificationResponse response = paymentService.processWebhook(gateway, payload);
        return ResponseEntity.ok(ApiResponse.success(response, "Webhook processed successfully"));
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get payment by transaction ID", description = "Retrieves payment status and gateway details by transaction ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Payment transaction not found")
    })
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByTransactionId(
            @Parameter(description = "Transaction ID", required = true) @PathVariable String transactionId) {
        PaymentResponse payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment details retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get payment by ID", description = "Retrieves master payment record by ID (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(
            @Parameter(description = "Payment ID", required = true) @PathVariable Long id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment details retrieved successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get paginated payments", description = "Retrieves paginated master listing of payments with gateway and status filters (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getPayments(
            @ModelAttribute PaymentFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<PaymentResponse> page = paymentService.getPayments(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Payments retrieved successfully"));
    }

    @PostMapping("/refund")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Process payment refund", description = "Triggers a full or partial refund for a successful payment (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Refund executed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid refund amount or non-refundable payment state")
    })
    public ResponseEntity<ApiResponse<RefundResponse>> refundPayment(
            @Valid @RequestBody RefundPaymentRequest request) {
        RefundResponse response = paymentService.refundPayment(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Refund executed successfully"));
    }
}
