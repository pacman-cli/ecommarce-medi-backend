package com.example.ecommerce.user.mapper;

import com.example.ecommerce.user.dto.request.AddressRequest;
import com.example.ecommerce.user.dto.response.AddressResponse;
import com.example.ecommerce.user.entity.Address;
import com.example.ecommerce.user.entity.User;
import org.springframework.stereotype.Component;

/**
 * Translates {@link Address} entities into response DTOs and requests into entities.
 */
@Component
public class AddressMapper {

    /**
     * Maps an address entity to its response DTO.
     *
     * @param address the persisted address
     * @return the response DTO
     */
    public AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .label(address.getLabel())
                .recipientName(address.getRecipientName())
                .phone(address.getPhone())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .isDefault(address.isDefault())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }

    /**
     * Maps a request DTO into a new address owned by the user.
     *
     * @param request the request DTO
     * @param user    the owning user
     * @return a transient address entity
     */
    public Address toEntity(AddressRequest request, User user) {
        return Address.builder()
                .user(user)
                .label(request.getLabel().trim())
                .recipientName(request.getRecipientName())
                .phone(request.getPhone())
                .street(request.getStreet().trim())
                .city(request.getCity().trim())
                .state(request.getState())
                .country(request.getCountry().trim())
                .postalCode(request.getPostalCode())
                .isDefault(request.isDefault())
                .build();
    }

    /**
     * Applies request values onto an existing managed address.
     *
     * @param address the existing address
     * @param request the request DTO
     */
    public void updateEntity(Address address, AddressRequest request) {
        address.setLabel(request.getLabel().trim());
        address.setRecipientName(request.getRecipientName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet().trim());
        address.setCity(request.getCity().trim());
        address.setState(request.getState());
        address.setCountry(request.getCountry().trim());
        address.setPostalCode(request.getPostalCode());
        address.setDefault(request.isDefault());
    }
}
