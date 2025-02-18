package com.culinaryapi.culinaryapi_user_Service.model;

import com.culinaryapi.culinaryapi_user_Service.dtos.UserServiceEventDto;
import com.culinaryapi.culinaryapi_user_Service.enums.AddressStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Table(name = "TB_Address")
@Entity
public class AddressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID addressId;

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @Enumerated(EnumType.STRING)
    private AddressStatus addressStatus;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserModel user;

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AddressStatus getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(AddressStatus addressStatus) {
        this.addressStatus = addressStatus;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UUID getUserId() {
        return user.getUserId();
    }

    public UserServiceEventDto convertToUserServiceEventDto() {
        var userServiceEventDto = new UserServiceEventDto();
        BeanUtils.copyProperties(this, userServiceEventDto);
        userServiceEventDto.setAddressStatus(this.addressStatus.toString());
        return userServiceEventDto;
    }
}