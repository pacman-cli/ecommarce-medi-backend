package com.example.ecommerce.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload required to place an order during checkout.
 */
@Schema(description = "Checkout request payload")
public class CheckoutRequest {

    @NotNull(message = "Shipping address is required")
    @Valid
    @Schema(description = "Shipping address details")
    private OrderAddressDto shippingAddress;

    @Valid
    @Schema(description = "Billing address details (optional if sameAsShipping is true)")
    private OrderAddressDto billingAddress;

    @Schema(description = "Indicates whether billing address is identical to shipping address", example = "true")
    private boolean sameAsShipping = true;

    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    @Schema(description = "Optional promotional coupon code to redeem", example = "SAVE20")
    private String couponCode;

    @Size(max = 500, message = "Order notes must not exceed 500 characters")
    @Schema(description = "Delivery instructions or order notes", example = "Please leave package at the front desk.")
    private String orderNotes;

    @Size(max = 100, message = "Session ID must not exceed 100 characters")
    @Schema(description = "Guest session ID (if checkout as unauthenticated guest)", example = "guest-session-12345")
    private String sessionId;

    public CheckoutRequest() {
    }

    public CheckoutRequest(OrderAddressDto shippingAddress, OrderAddressDto billingAddress, boolean sameAsShipping, String couponCode, String orderNotes, String sessionId) {
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.sameAsShipping = sameAsShipping;
        this.couponCode = couponCode;
        this.orderNotes = orderNotes;
        this.sessionId = sessionId;
    }

    public OrderAddressDto getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(OrderAddressDto shippingAddress) { this.shippingAddress = shippingAddress; }

    public OrderAddressDto getBillingAddress() { return billingAddress; }
    public void setBillingAddress(OrderAddressDto billingAddress) { this.billingAddress = billingAddress; }

    public boolean isSameAsShipping() { return sameAsShipping; }
    public void setSameAsShipping(boolean sameAsShipping) { this.sameAsShipping = sameAsShipping; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public String getOrderNotes() { return orderNotes; }
    public void setOrderNotes(String orderNotes) { this.orderNotes = orderNotes; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public static CheckoutRequestBuilder builder() { return new CheckoutRequestBuilder(); }

    public static class CheckoutRequestBuilder {
        private OrderAddressDto shippingAddress;
        private OrderAddressDto billingAddress;
        private boolean sameAsShipping = true;
        private String couponCode;
        private String orderNotes;
        private String sessionId;

        CheckoutRequestBuilder() {}

        public CheckoutRequestBuilder shippingAddress(OrderAddressDto shippingAddress) { this.shippingAddress = shippingAddress; return this; }
        public CheckoutRequestBuilder billingAddress(OrderAddressDto billingAddress) { this.billingAddress = billingAddress; return this; }
        public CheckoutRequestBuilder sameAsShipping(boolean sameAsShipping) { this.sameAsShipping = sameAsShipping; return this; }
        public CheckoutRequestBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public CheckoutRequestBuilder orderNotes(String orderNotes) { this.orderNotes = orderNotes; return this; }
        public CheckoutRequestBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }

        public CheckoutRequest build() {
            return new CheckoutRequest(shippingAddress, billingAddress, sameAsShipping, couponCode, orderNotes, sessionId);
        }
    }
}
