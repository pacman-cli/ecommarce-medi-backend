package com.example.ecommerce.inventory.validator;

import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.inventory.dto.request.WarehouseRequest;
import com.example.ecommerce.inventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Enforces warehouse code and name uniqueness.
 */
@Component
@RequiredArgsConstructor
public class WarehouseValidator {

    private final WarehouseRepository warehouseRepository;

    public void validateForCreate(WarehouseRequest request) {
        validateCodeUniqueness(request.getCode(), null);
        validateNameUniqueness(request.getName(), null);
    }

    public void validateForUpdate(Long id, WarehouseRequest request) {
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
                ? warehouseRepository.existsByCodeIgnoreCase(trimmed)
                : warehouseRepository.existsByCodeIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Warehouse code '" + trimmed + "' already exists");
        }
    }

    public void validateNameUniqueness(String name, Long excludeId) {
        String trimmed = name.trim();
        boolean exists = excludeId == null
                ? warehouseRepository.existsByNameIgnoreCase(trimmed)
                : warehouseRepository.existsByNameIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Warehouse name '" + trimmed + "' already exists");
        }
    }
}
