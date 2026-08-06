package com.example.ecommerce.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Address response projection for order shipping and billing addresses.
 */
@Schema(description = "Order address details response")
public class OrderAddressResponse {

    @Schema(description = "Recipient name", example = "John Doe")
    private String recipientName;

    @Schema(description = "Contact phone", example = "+1-555-019-2834")
    private String phone;

    @Schema(description = "Street address", example = "742 Evergreen Terrace")
    private String streetAddress;

    @Schema(description = "City", example = "Springfield")
    private String city;

    @Schema(description = "State/Province", example = "OR")
    private String state;

    @Schema(description = "Zip/Postal code", example = "97477")
    private String zipCode;

    @Schema(description = "Country", example = "United States")
    private String country;

    public OrderAddressResponse() {
    }

    public OrderAddressResponse(String recipientName, String phone, String streetAddress, String city, String state, String zipCode, String country) {
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

    public static OrderAddressResponseBuilder builder() { return new OrderAddressResponseBuilder(); }

    public static class OrderAddressResponseBuilder {
        private String recipientName;
        private String phone;
        private String streetAddress;
        private String city;
        private String state;
        private String zipCode;
        private String country;

        OrderAddressResponseBuilder() {}

        public OrderAddressResponseBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        public OrderAddressResponseBuilder phone(String phone) { this.phone = phone; return this; }
        public OrderAddressResponseBuilder streetAddress(String streetAddress) { this.streetAddress = streetAddress; return this; }
        public OrderAddressResponseBuilder city(String city) { this.city = city; return this; }
        public OrderAddressResponseBuilder state(String state) { this.state = state; return this; }
        public OrderAddressResponseBuilder zipCode(String zipCode) { this.zipCode = zipCode; return this; }
        public OrderAddressResponseBuilder country(String country) { this.country = country; return this; }

        public OrderAddressResponse build() {
            return new OrderAddressResponse(recipientName, phone, streetAddress, city, state, zipCode, country);
        }
    }
}
