package com.culinaryapi.culinaryapi_user_Service.controllers;

import com.culinaryapi.culinaryapi_user_Service.configs.security.AuthenticationCurrentUserService;
import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import com.culinaryapi.culinaryapi_user_Service.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController( AddressService addressService) {
        this.addressService = addressService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PostMapping("/")
    public ResponseEntity<Object> createAddress(@RequestBody @Validated AddressDto addressDto) {
        return addressService.createAddress(addressDto);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PutMapping("/{addressId}/deactivate")
    public ResponseEntity<Object> deactivateAddress(@PathVariable(value="addressId") UUID addressId){
        return addressService.deactivateAddress(addressId);

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PutMapping("/{addressId}")
    public ResponseEntity<Object> updateAddress(@PathVariable(value="addressId") UUID addressId, @RequestBody @Validated AddressDto addressDto){
        return addressService.updateAddress(addressId,addressDto);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Page<AddressModel>> getUserAddresses(
            @PathVariable UUID userId, Pageable pageable) {

            return addressService.getUserAddresses(userId, pageable);
    }
}
