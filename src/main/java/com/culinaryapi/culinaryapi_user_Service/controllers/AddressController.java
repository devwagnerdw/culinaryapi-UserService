package com.culinaryapi.culinaryapi_user_Service.controllers;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import com.culinaryapi.culinaryapi_user_Service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final UserService userService;
    private final AddressService addressService;

    public AddressController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createAddress(@RequestBody @Validated AddressDto addressDto) {
        return addressService.createAddress(addressDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserWithAddresses(@PathVariable(value = "userId") UUID userId) {
      return   userService.getUserWithAddresses(userId);
    }
}
