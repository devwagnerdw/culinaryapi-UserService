package com.culinaryapi.culinaryapi_user_Service.services.impl;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.enums.ActionType;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.publishers.UserEventPublisher;
import com.culinaryapi.culinaryapi_user_Service.repositories.AddressRepository;
import com.culinaryapi.culinaryapi_user_Service.repositories.UserRepository;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Optional<UserModel> optionalUserModel = userRepository.findById(addressDto.getUserId());
        if (optionalUserModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }
        var addressModel = new AddressModel();

        BeanUtils.copyProperties(addressDto, addressModel);

        UserModel userModel = optionalUserModel.get();
        if (userModel.getAddresses().size() >= 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User cannot have more than 3 addresses");
        }

        addressModel.setUser(userModel);
        addressRepository.save(addressModel);
        userEventPublisher.publishUserEvent(addressModel.convertToUserServiceEventDto(),ActionType.CREATE);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressModel);
    }


    @Override
    public ResponseEntity<Object> deleteAddress(UUID addressId) {
        Optional<AddressModel> optionalAddressModel= addressRepository.findById(addressId);
        if (optionalAddressModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ADDRESS NOT FOUND");
        }
        addressRepository.deleteById(addressId);
        return ResponseEntity.status(HttpStatus.OK).body("Address deleted successfully.");
    }


    @Override
    public ResponseEntity<Object> updateAddress(UUID addressId, AddressDto addressDto) {
        Optional<AddressModel> optionalAddressModel = addressRepository.findById(addressId);
        if (optionalAddressModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        AddressModel addressModel = optionalAddressModel.get();
        addressModel.setStreet(addressDto.getStreet());
        addressModel.setCity(addressDto.getCity());
        addressModel.setState(addressDto.getState());
        addressModel.setPostalCode(addressDto.getPostalCode());
        addressModel.setCountry(addressDto.getCountry());
        addressRepository.save(addressModel);
        return ResponseEntity.status(HttpStatus.OK).body(addressModel);
    }


    @Override
    public ResponseEntity<Page<AddressModel>> getUserAddresses(UUID userId, Pageable pageable) {
        Page<AddressModel> addresses = addressRepository.findByUserUserId(userId, pageable);
        if (addresses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(addresses);
    }

}
