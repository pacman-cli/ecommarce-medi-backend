package com.example.ecommerce.order.validator;

import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.order.entity.OrderStatus;
import org.springframework.stereotype.Component;

/**
 * Validates checkout eligibility and order lifecycle state transitions.
 */
@Component
public class OrderValidator {

    public void validateCartForCheckout(Cart cart) {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot place order with an empty cart");
        }
    }

    public void validateStatusTransition(OrderStatus currentStatus, OrderStatus targetStatus) {
        if (currentStatus == targetStatus) {
            return;
        }

        if (currentStatus == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot change status of a CANCELLED order");
        }

        if (currentStatus == OrderStatus.DELIVERED && targetStatus != OrderStatus.RETURNED && targetStatus != OrderStatus.REFUNDED) {
            throw new BadRequestException("Delivered orders can only be updated to RETURNED or REFUNDED");
        }

        if (currentStatus == OrderStatus.REFUNDED) {
            throw new BadRequestException("Cannot change status of a REFUNDED order");
        }
    }
}
