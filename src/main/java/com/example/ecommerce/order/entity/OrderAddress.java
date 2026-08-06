package com.example.ecommerce.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Embedded snapshot address used for shipping or billing on an order.
 */
@Embeddable
public class OrderAddress {

    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "street_address", nullable = false, length = 255)
    private String streetAddress;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    public OrderAddress() {
    }

    public OrderAddress(String recipientName, String phone, String streetAddress, String city, String state, String zipCode, String country) {
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

    public static OrderAddressBuilder builder() { return new OrderAddressBuilder(); }

    public static class OrderAddressBuilder {
        private String recipientName;
        private String phone;
        private String streetAddress;
        private String city;
        private String state;
        private String zipCode;
        private String country;

        OrderAddressBuilder() {}

        public OrderAddressBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        public OrderAddressBuilder phone(String phone) { this.phone = phone; return this; }
        public OrderAddressBuilder streetAddress(String streetAddress) { this.streetAddress = streetAddress; return this; }
        public OrderAddressBuilder city(String city) { this.city = city; return this; }
        public OrderAddressBuilder state(String state) { this.state = state; return this; }
        public OrderAddressBuilder zipCode(String zipCode) { this.zipCode = zipCode; return this; }
        public OrderAddressBuilder country(String country) { this.country = country; return this; }

        public OrderAddress build() {
            return new OrderAddress(recipientName, phone, streetAddress, city, state, zipCode, country);
        }
    }
}
