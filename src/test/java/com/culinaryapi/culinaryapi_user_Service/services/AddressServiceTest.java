package com.culinaryapi.culinaryapi_user_Service.services;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.enums.AddressStatus;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.publishers.UserEventPublisher;
import com.culinaryapi.culinaryapi_user_Service.repositories.AddressRepository;
import com.culinaryapi.culinaryapi_user_Service.repositories.UserRepository;
import com.culinaryapi.culinaryapi_user_Service.services.impl.AddressServiceImpl;
import com.culinaryapi.culinaryapi_user_Service.utils.PermissionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class AddressServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PermissionUtils permissionUtils;

    @Mock
    private UserEventPublisher userEventPublisher;

    @InjectMocks private AddressServiceImpl addressService;



    @Test
    @DisplayName("Should return CREATED when address is successfully created")
    void createAddress_success() {
        UUID userId = UUID.randomUUID();
        var userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setUserStatus("ACTIVE");

        var addressDto = new AddressDto();
        addressDto.setUserId(userId);
        addressDto.setStreet("123 Main St");
        addressDto.setCity("New York");
        addressDto.setState("NY");
        addressDto.setPostalCode("12663521");
        addressDto.setCountry("USA");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(permissionUtils.hasPermission(userId)).thenReturn(true);
        when(addressRepository.save(any(AddressModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Object> response = addressService.createAddress(addressDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(AddressModel.class);

        AddressModel address = (AddressModel) response.getBody();
        assertThat(address.getStreet()).isEqualTo("123 Main St");
        assertThat(address.getCity()).isEqualTo("New York");
        assertThat(address.getState()).isEqualTo("NY");
        assertThat(address.getPostalCode()).isEqualTo("12663521");
        assertThat(address.getCountry()).isEqualTo("USA");
        assertThat(address.getAddressStatus()).isEqualTo(AddressStatus.ACTIVE);
        assertThat(address.getUser()).isEqualTo(userModel);
    }



    @Test
    @DisplayName("Should return 403 when user has no permission")
    void createAddress_whenNoPermission_shouldReturnForbidden() {
        UUID userId = UUID.randomUUID();
        var userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setUserStatus("ACTIVE");

        var addressDto = new AddressDto();
        addressDto.setUserId(userId);
        addressDto.setStreet("123 Main St");
        addressDto.setCity("New York");
        addressDto.setState("NY");
        addressDto.setPostalCode("12663521");
        addressDto.setCountry("USA");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(permissionUtils.hasPermission(userId)).thenReturn(false);

        ResponseEntity<Object> response = addressService.createAddress(addressDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isEqualTo("You do not have permission to create an address for another user.");
    }
}
