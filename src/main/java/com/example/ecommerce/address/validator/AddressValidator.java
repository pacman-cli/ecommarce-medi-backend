package com.example.ecommerce.address.validator;

import com.example.ecommerce.address.dto.request.AddressRequest;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.UnauthorizedException;
import com.example.ecommerce.user.entity.Address;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Component providing validation logic for address input payloads and ownership rules.
 */
@Component
public class AddressValidator {

    /**
     * Validates input payload coordinates, postal code, and required fields.
     */
    public void validateRequest(AddressRequest request) {
        if (request == null) {
            throw new BadRequestException("Address request payload must not be null");
        }

        if (request.getLatitude() != null) {
            if (request.getLatitude() < -90.0 || request.getLatitude() > 90.0) {
                throw new BadRequestException("Latitude coordinate must be between -90.0 and 90.0");
            }
        }

        if (request.getLongitude() != null) {
            if (request.getLongitude() < -180.0 || request.getLongitude() > 180.0) {
                throw new BadRequestException("Longitude coordinate must be between -180.0 and 180.0");
            }
        }

        if (StringUtils.hasText(request.getPostalCode())) {
            if (request.getPostalCode().length() < 3 || request.getPostalCode().length() > 20) {
                throw new BadRequestException("Postal code length must be between 3 and 20 characters");
            }
        }
    }

    /**
     * Verifies that the specified address belongs to the authenticated user ID.
     */
    public void verifyOwnership(Address address, Long currentUserId) {
        if (address == null || address.getUser() == null) {
            throw new BadRequestException("Invalid address entity or user association");
        }
        if (currentUserId == null || !address.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException("You are not authorized to access or modify this address");
        }
    }
}
