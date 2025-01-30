package com.culinaryapi.culinaryapi_user_Service.services;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import org.springframework.http.ResponseEntity;

public interface AddressService {

    ResponseEntity<Object> createAddress(AddressDto addressDto);

}