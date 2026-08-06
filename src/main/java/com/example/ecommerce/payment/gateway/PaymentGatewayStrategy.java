package com.example.ecommerce.payment.gateway;

import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.payment.dto.response.PaymentInitiationResponse;
import com.example.ecommerce.payment.dto.response.PaymentVerificationResponse;
import com.example.ecommerce.payment.dto.response.RefundResponse;
import com.example.ecommerce.payment.entity.Payment;
import com.example.ecommerce.payment.entity.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Strategy contract implemented by payment gateway integrations (SSLCommerz, bKash, Nagad, COD).
 */
public interface PaymentGatewayStrategy {

    PaymentMethod getSupportedMethod();

    PaymentInitiationResponse initiatePayment(Payment payment, Order order, String returnUrl, String cancelUrl);

    PaymentVerificationResponse verifyPayment(Payment payment, Map<String, String> queryParams);

    RefundResponse processRefund(Payment payment, BigDecimal amount, String reason);
}
