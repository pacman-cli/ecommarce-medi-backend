package com.example.ecommerce.inventory.validator;

import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.inventory.dto.request.SupplierRequest;
import com.example.ecommerce.inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Enforces supplier code and name uniqueness.
 */
@Component("inventorySupplierValidator")
@RequiredArgsConstructor
public class SupplierValidator {

    private final SupplierRepository supplierRepository;

    public void validateForCreate(SupplierRequest request) {
        validateCodeUniqueness(request.getCode(), null);
        validateNameUniqueness(request.getName(), null);
    }

    public void validateForUpdate(Long id, SupplierRequest request) {
        if (StringUtils.hasText(request.getCode())) {
            validateCodeUniqueness(request.getCode(), id);
        }
        if (StringUtils.hasText(request.getName())) {
            validateNameUniqueness(request.getName(), id);
        }
    }

    public void validateCodeUniqueness(String code, Long excludeId) {
        String trimmed = code.trim();
        boolean exists = excludeId == null
                ? supplierRepository.existsByCodeIgnoreCase(trimmed)
                : supplierRepository.existsByCodeIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Supplier code '" + trimmed + "' already exists");
        }
    }

    public void validateNameUniqueness(String name, Long excludeId) {
        String trimmed = name.trim();
        boolean exists = excludeId == null
                ? supplierRepository.existsByNameIgnoreCase(trimmed)
                : supplierRepository.existsByNameIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Supplier name '" + trimmed + "' already exists");
        }
    }
}
