package com.culinaryapi.culinaryapi_user_Service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS")
public class UserModel {
    @Id
    private UUID userId;
    private String email;
    private String phoneNumber;
    private String userStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<AddressModel> addresses;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Set<AddressModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressModel> addresses) {
        this.addresses = addresses;
    }
}