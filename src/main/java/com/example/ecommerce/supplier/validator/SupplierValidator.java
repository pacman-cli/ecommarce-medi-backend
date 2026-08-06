package com.example.ecommerce.supplier.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.supplier.dto.request.SupplierRequest;
import com.example.ecommerce.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Component for enforcing supplier uniqueness and business rule validation.
 */
@Component("vendorSupplierValidator")
public class SupplierValidator {

    private final SupplierRepository supplierRepository;

    public SupplierValidator(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public void validateCreate(SupplierRequest request) {
        if (supplierRepository.existsByCodeAndDeletedFalse(request.getCode())) {
            throw new BadRequestException("Supplier code already exists: " + request.getCode());
        }

        if (supplierRepository.existsByNameAndDeletedFalse(request.getName())) {
            throw new BadRequestException("Supplier company name already exists: " + request.getName());
        }

        if (StringUtils.hasText(request.getEmail()) && supplierRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new BadRequestException("Supplier email already exists: " + request.getEmail());
        }
    }

    public void validateUpdate(Long id, SupplierRequest request) {
        Optional<Supplier> existingCode = supplierRepository.findByCodeAndDeletedFalse(request.getCode());
        if (existingCode.isPresent() && !existingCode.get().getId().equals(id)) {
            throw new BadRequestException("Supplier code already in use by another vendor: " + request.getCode());
        }

        Optional<Supplier> existingName = supplierRepository.findByNameAndDeletedFalse(request.getName());
        if (existingName.isPresent() && !existingName.get().getId().equals(id)) {
            throw new BadRequestException("Supplier company name already in use by another vendor: " + request.getName());
        }
    }
}
