package com.culinaryapi.culinaryapi_user_Service.model;


import jakarta.persistence.*;
import java.util.List;
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
    private List<AddressModel> addresses;

}