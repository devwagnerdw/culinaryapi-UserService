package com.culinaryapi.culinaryapi_user_Service.controllers;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import com.culinaryapi.culinaryapi_user_Service.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
    public ResponseEntity<Object> createAddress(@RequestBody @Validated AddressDto addressDto){

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

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserWithAddresses(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }
}
