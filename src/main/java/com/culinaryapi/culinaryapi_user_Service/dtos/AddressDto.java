package com.culinaryapi.culinaryapi_user_Service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AddressDto {



    @NotNull(message = "ID cannot be null")
    private UUID userId;

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street must be at most 255 characters")
    String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City must be at most 100 characters")
    String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State must be at most 100 characters")
    String state;

    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 20, message = "Postal code must be at most 20 characters")
    String postalCode;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country must be at most 100 characters")
    String country;

    public @NotNull(message = "ID cannot be null") UUID getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "ID cannot be null") UUID userId) {
        this.userId = userId;
    }

    public  String getStreet() {
        return street;
    }

    public void setStreet (String street) {
        this.street = street;
    }

    public   String getCity() {
        return city;
    }

    public void setCity ( String city) {
        this.city = city;
    }

    public  String getState() {
        return state;
    }

    public void setState (String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode ( String postalCode) {
        this.postalCode = postalCode;
    }

    public  String getCountry() {
        return country;
    }

    public void setCountry( String countr) {
        this.country = countr;
    }
}
