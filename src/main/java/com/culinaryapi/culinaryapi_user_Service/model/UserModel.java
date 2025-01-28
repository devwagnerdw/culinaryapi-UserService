package com.culinaryapi.culinaryapi_user_Service.model;


import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS")
public class UserModel {
    @Id
    private UUID userId;
    private String username;
    private String phoneNumber;
    private String userStatus ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AddressModel> addresses;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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