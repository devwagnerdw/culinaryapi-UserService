package com.culinaryapi.culinaryapi_user_Service.services;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AddressService {

    ResponseEntity<Object> createAddress(AddressDto addressDto);

    ResponseEntity<Object> updateAddress(UUID addressId, AddressDto addressDto);

    ResponseEntity<Object> deleteAddress(UUID addressId);

    ResponseEntity<Page<AddressModel>> getUserAddresses(UUID userId, Pageable pageable);
}