package com.culinaryapi.culinaryapi_user_Service.controllers;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.repositories.AddressRepository;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import com.culinaryapi.culinaryapi_user_Service.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/addresses")
public class AdressController {

    private final UserService userService;
    private final AddressService addressService;

    public AdressController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> registerAddress(@RequestBody @Validated AddressDto addressDto){

        Optional<UserModel> optionalUserModel= userService.findById(addressDto.getId());
        if (optionalUserModel.isEmpty()){
            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }else {
          var addressModel = new AddressModel();
            BeanUtils.copyProperties(addressDto ,addressModel);
            addressModel.setUser(optionalUserModel.get());
            addressService.save(addressModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(addressModel);
        }

    }
}
