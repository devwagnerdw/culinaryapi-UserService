package com.culinaryapi.culinaryapi_user_Service.services.impl;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.enums.ActionType;
import com.culinaryapi.culinaryapi_user_Service.enums.AddressStatus;
import com.culinaryapi.culinaryapi_user_Service.exception.NotFoundException;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.publishers.UserEventPublisher;
import com.culinaryapi.culinaryapi_user_Service.repositories.AddressRepository;
import com.culinaryapi.culinaryapi_user_Service.repositories.UserRepository;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import com.culinaryapi.culinaryapi_user_Service.utils.PermissionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;
    private final PermissionUtils permissionUtils;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository, UserEventPublisher userEventPublisher, PermissionUtils permissionUtils) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
        this.permissionUtils = permissionUtils;
    }

    @Override
    public ResponseEntity<Object> createAddress(AddressDto addressDto) {
        UserModel userModel = userRepository.findById(addressDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + addressDto.getUserId()));

        if (!permissionUtils.hasPermission(userModel.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to create an address for another user.");
        }

        if ("BLOCKED".equalsIgnoreCase(userModel.getUserStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User is blocked and cannot add an address.");
        }

        var addressModel = new AddressModel();
        BeanUtils.copyProperties(addressDto, addressModel);

        addressModel.setUser(userModel);
        addressModel.setAddressStatus(AddressStatus.ACTIVE);
        addressRepository.save(addressModel);
        userEventPublisher.publishUserEvent(addressModel.convertToUserServiceEventDto(),ActionType.CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressModel);
    }

    @Override
    public ResponseEntity<Object> deactivateAddress(UUID addressId) {
        AddressModel addressModel= addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException ("ADDRESS NOT FOUND"));

        if (!permissionUtils.hasPermission(addressModel.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to deactivate this address.");
        }

        long activeAddressCount = addressRepository.
                countByUserUserIdAndAddressStatus(addressModel.getUserId(), AddressStatus.ACTIVE);
        if (activeAddressCount == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot deactivate this address because you must have at least one active address.");
        }

        addressModel.setAddressStatus(AddressStatus.BLOCKED);
        addressRepository.save(addressModel);
        userEventPublisher.publishUserEvent(addressModel.convertToUserServiceEventDto(),ActionType.UPDATE);
        return ResponseEntity.status(HttpStatus.OK).body("Address blocked successfully.");
    }


    @Override
    public ResponseEntity<Object> updateAddress(UUID addressId, AddressDto addressDto) {
        AddressModel addressModel= addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("ADDRESS NOT FOUND"));

        if (!permissionUtils.hasPermission(addressModel.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to update this address.");
        }
        addressModel.setStreet(addressDto.getStreet());
        addressModel.setCity(addressDto.getCity());
        addressModel.setState(addressDto.getState());
        addressModel.setPostalCode(addressDto.getPostalCode());
        addressModel.setCountry(addressDto.getCountry());
        addressRepository.save(addressModel);
        userEventPublisher.publishUserEvent(addressModel.convertToUserServiceEventDto(),ActionType.UPDATE);
        return ResponseEntity.status(HttpStatus.OK).body(addressModel);
    }


    @Override
    public ResponseEntity<Page<AddressModel>> getUserAddresses(UUID userId, Pageable pageable) {
        if (!permissionUtils.hasPermission(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<AddressModel> addresses = addressRepository.findByUserUserIdAndAddressStatus(userId, AddressStatus.ACTIVE, pageable);

        return ResponseEntity.ok(addresses);
    }

}
