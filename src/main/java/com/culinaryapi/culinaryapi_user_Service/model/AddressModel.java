package com.culinaryapi.culinaryapi_user_Service.model;

import jakarta.persistence.*;


@Table(name = "TB_Address")
@Entity
public class AddressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
}