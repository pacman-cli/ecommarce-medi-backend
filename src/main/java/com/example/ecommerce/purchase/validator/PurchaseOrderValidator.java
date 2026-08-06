package com.example.ecommerce.purchase.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.purchase.dto.enums.PurchaseStatus;
import com.example.ecommerce.purchase.entity.PurchaseOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Component providing validation logic for purchase order lifecycle state transitions,
 * receiving quantities, and payment recordings.
 */
@Component
public class PurchaseOrderValidator {

    public void validateModifiable(PurchaseOrder po) {
        if (po.getStatus() != PurchaseStatus.DRAFT) {
            throw new BadRequestException("Purchase order PO #" + po.getPoNumber() +
                    " cannot be modified when in status: " + po.getStatus() + ". Only DRAFT orders are modifiable.");
        }
    }

    public void validateSubmittable(PurchaseOrder po) {
        if (po.getStatus() != PurchaseStatus.DRAFT) {
            throw new BadRequestException("Only DRAFT purchase orders can be submitted.");
        }
        if (po.getItems() == null || po.getItems().isEmpty()) {
            throw new BadRequestException("Purchase order must contain at least one line item before submitting.");
        }
    }

    public void validateReceivable(PurchaseOrder po) {
        if (po.getStatus() != PurchaseStatus.ORDERED && po.getStatus() != PurchaseStatus.PARTIALLY_RECEIVED) {
            throw new BadRequestException("Purchase order PO #" + po.getPoNumber() +
                    " is not in a receivable status (Current: " + po.getStatus() + "). Must be ORDERED or PARTIALLY_RECEIVED.");
        }
    }

    public void validatePayment(PurchaseOrder po, BigDecimal amount) {
        if (po.getStatus() == PurchaseStatus.CANCELLED) {
            throw new BadRequestException("Cannot record payment for CANCELLED purchase order.");
        }
        BigDecimal remaining = po.getTotalAmount().subtract(po.getPaidAmount());
        if (amount.compareTo(remaining) > 0) {
            throw new BadRequestException(String.format("Payment amount (%.2f) exceeds remaining unpaid invoice balance (%.2f)",
                    amount, remaining));
        }
    }
}
