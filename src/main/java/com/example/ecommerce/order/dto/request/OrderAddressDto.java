package com.example.ecommerce.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Address DTO for checkout shipping and billing payloads.
 */
@Schema(description = "Order address details")
public class OrderAddressDto {

    @NotBlank(message = "Recipient name is required")
    @Size(max = 100, message = "Recipient name must not exceed 100 characters")
    @Schema(description = "Recipient full name", example = "John Doe")
    private String recipientName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Schema(description = "Contact phone number", example = "+1-555-019-2834")
    private String phone;

    @NotBlank(message = "Street address is required")
    @Size(max = 255, message = "Street address must not exceed 255 characters")
    @Schema(description = "Street address line", example = "742 Evergreen Terrace")
    private String streetAddress;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    @Schema(description = "City", example = "Springfield")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    @Schema(description = "State or Province", example = "OR")
    private String state;

    @NotBlank(message = "Zip/Postal code is required")
    @Size(max = 20, message = "Zip code must not exceed 20 characters")
    @Schema(description = "Zip/Postal code", example = "97477")
    private String zipCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    @Schema(description = "Country", example = "United States")
    private String country;

    public OrderAddressDto() {
    }

    public OrderAddressDto(String recipientName, String phone, String streetAddress, String city, String state, String zipCode, String country) {
        this.recipientName = recipientName;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public static OrderAddressDtoBuilder builder() { return new OrderAddressDtoBuilder(); }

    public static class OrderAddressDtoBuilder {
        private String recipientName;
        private String phone;
        private String streetAddress;
        private String city;
        private String state;
        private String zipCode;
        private String country;

        OrderAddressDtoBuilder() {}

        public OrderAddressDtoBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        public OrderAddressDtoBuilder phone(String phone) { this.phone = phone; return this; }
        public OrderAddressDtoBuilder streetAddress(String streetAddress) { this.streetAddress = streetAddress; return this; }
        public OrderAddressDtoBuilder city(String city) { this.city = city; return this; }
        public OrderAddressDtoBuilder state(String state) { this.state = state; return this; }
        public OrderAddressDtoBuilder zipCode(String zipCode) { this.zipCode = zipCode; return this; }
        public OrderAddressDtoBuilder country(String country) { this.country = country; return this; }

        public OrderAddressDto build() {
            return new OrderAddressDto(recipientName, phone, streetAddress, city, state, zipCode, country);
        }
    }
}
