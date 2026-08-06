package com.example.ecommerce.inventory.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.inventory.dto.request.InventoryAdjustmentRequest;
import com.example.ecommerce.inventory.dto.request.StockBatchRequest;
import com.example.ecommerce.inventory.entity.StockBatch;
import com.example.ecommerce.inventory.entity.TransactionType;
import com.example.ecommerce.inventory.repository.StockBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Validator enforcing inventory stock batch limits, expiration dates and stock adjustment rules.
 */
@Component
@RequiredArgsConstructor
public class InventoryValidator {

    private final StockBatchRepository stockBatchRepository;

    public void validateStockBatchForCreate(StockBatchRequest request) {
        if (request.getManufacturingDate() != null && request.getExpiryDate() != null) {
            if (request.getManufacturingDate().isAfter(request.getExpiryDate())) {
                throw new BadRequestException("Manufacturing date cannot be after expiry date");
            }
        }
        if (request.getExpiryDate() != null && request.getExpiryDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot receive an already expired stock batch");
        }
        boolean exists = stockBatchRepository.existsByBatchNumberAndProductIdAndWarehouseIdAndDeletedFalse(
                request.getBatchNumber().trim(), request.getProductId(), request.getWarehouseId()
        );
        if (exists) {
            throw new ConflictException("Batch '" + request.getBatchNumber() + "' already exists for this product in the selected warehouse");
        }
    }

    public void validateAdjustment(InventoryAdjustmentRequest request, StockBatch batch) {
        if (request.getTransactionType() == TransactionType.ADJUSTMENT_DECREASE || request.getTransactionType() == TransactionType.EXPIRED_DISCARD) {
            if (batch != null && batch.getAvailableQuantity() < request.getQuantity()) {
                throw new BadRequestException("Insufficient available batch stock quantity. Available: " + batch.getAvailableQuantity() + ", requested: " + request.getQuantity());
            }
        }
    }
}
