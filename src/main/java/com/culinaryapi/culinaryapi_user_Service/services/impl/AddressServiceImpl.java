package com.culinaryapi.culinaryapi_user_Service.services.impl;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.enums.ActionType;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.publishers.UserEventPublisher;
import com.culinaryapi.culinaryapi_user_Service.repositories.AddressRepository;
import com.culinaryapi.culinaryapi_user_Service.repositories.UserRepository;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import com.culinaryapi.culinaryapi_user_Service.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository, UserEventPublisher userEventPublisher) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
    }

    @Override
    public ResponseEntity<Object> createAddress(AddressDto addressDto) {
        Optional<UserModel> optionalUserModel = userRepository.findById(addressDto.getId());

        if (optionalUserModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }

        var addressModel = new AddressModel();
        BeanUtils.copyProperties(addressDto, addressModel);
        addressModel.setUser(optionalUserModel.get());
        addressRepository.save(addressModel);
        userEventPublisher.publishUserEvent(addressModel.convertToUserServiceEventDto(), ActionType.CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressModel);
    }

}
