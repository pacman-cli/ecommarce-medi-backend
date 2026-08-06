package com.example.ecommerce.payment.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.payment.dto.request.InitiatePaymentRequest;
import com.example.ecommerce.payment.dto.request.PaymentFilterRequest;
import com.example.ecommerce.payment.dto.request.RefundPaymentRequest;
import com.example.ecommerce.payment.dto.request.VerifyPaymentRequest;
import com.example.ecommerce.payment.dto.response.PaymentInitiationResponse;
import com.example.ecommerce.payment.dto.response.PaymentResponse;
import com.example.ecommerce.payment.dto.response.PaymentVerificationResponse;
import com.example.ecommerce.payment.dto.response.RefundResponse;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Service interface managing multi-gateway online payment initiation, IPN webhook handling,
 * transaction verification, payment audit logs and refunds.
 */
public interface PaymentService {

    PaymentInitiationResponse initiatePayment(InitiatePaymentRequest request);

    PaymentVerificationResponse verifyPayment(VerifyPaymentRequest request, Map<String, String> queryParams);

    PaymentVerificationResponse processWebhook(String gateway, Map<String, String> payload);

    RefundResponse refundPayment(RefundPaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByTransactionId(String transactionId);

    PageResponse<PaymentResponse> getPayments(PaymentFilterRequest filter, Pageable pageable);
}
