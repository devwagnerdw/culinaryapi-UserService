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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
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

    @InjectMocks
    private AddressServiceImpl addressService;

    @Nested
    @DisplayName("Tests for createAddress")
    class CreateAddressTests {

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
        @DisplayName("Should return 403 Forbidden if the user does not have permission to create an address for another user")
        void shouldReturnForbiddenWhenUserDoesNotHavePermissionToCreateAddressForAnotherUser() {
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
            assertThat(response.getBody()).isNotNull();
        }

        @Test
        @DisplayName("Should return 403 Forbidden when user is blocked")
        void shouldReturnForbiddenWhenUserIsBlocked() {
            UUID userId = UUID.randomUUID();
            var userModel = new UserModel();
            userModel.setUserId(userId);
            userModel.setUserStatus("BLOCKED");

            var addressDto = new AddressDto();
            addressDto.setUserId(userId);
            addressDto.setStreet("123 Main St");
            addressDto.setCity("New York");
            addressDto.setState("NY");
            addressDto.setPostalCode("12663521");
            addressDto.setCountry("USA");

            when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
            when(permissionUtils.hasPermission(userId)).thenReturn(true);

            ResponseEntity<Object> response = addressService.createAddress(addressDto);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    @DisplayName("Tests for deactivateAddress")
    class DeactivateAddressTests{

        @Test
        void shouldReturnForbiddenWhenUserDoesNotHavePermissionToDeactivateAddress(){
            UUID userId = UUID.randomUUID();
            var userModel = new UserModel();
            userModel.setUserId(userId);
            userModel.setUserStatus("ACTIVE");

            var addressModel = new AddressModel();

            addressModel.setAddressId(UUID.randomUUID());
            addressModel.setStreet("123 Main St");
            addressModel.setCity("New York");
            addressModel.setState("NY");
            addressModel.setPostalCode("12663521");
            addressModel.setCountry("USA");
            addressModel.setUser(userModel);

            when(addressRepository.findById(addressModel.getAddressId())).thenReturn(Optional.of(addressModel));
            when(permissionUtils.hasPermission(userModel.getUserId())).thenReturn(false);

            ResponseEntity<Object> response = addressService.deactivateAddress(addressModel.getAddressId());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("Should return BAD_REQUEST when trying to deactivate the user's only active address")
        void shouldReturnBadRequestWhenTryingToDeactivateOnlyActiveAddress() {
            UUID userId = UUID.randomUUID();
            var userModel = new UserModel();
            userModel.setUserId(userId);
            userModel.setUserStatus("ACTIVE");

            var addressModel = new AddressModel();

            addressModel.setAddressId(UUID.randomUUID());
            addressModel.setStreet("123 Main St");
            addressModel.setCity("New York");
            addressModel.setState("NY");
            addressModel.setPostalCode("12663521");
            addressModel.setCountry("USA");
            addressModel.setUser(userModel);

            when(addressRepository.findById(addressModel.getAddressId())).thenReturn(Optional.of(addressModel));
            when(permissionUtils.hasPermission(userModel.getUserId())).thenReturn(true);
            when(addressRepository.countByUserUserIdAndAddressStatus(addressModel.getUserId(),AddressStatus.ACTIVE)).thenReturn(1L);

            ResponseEntity<Object> response = addressService.deactivateAddress(addressModel.getAddressId());

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        }

        @Test
        @DisplayName("Should deactivate the address successfully when the user has permission and has more than one active address")
        void shouldDeactivateAddressSuccessfully() {
            UUID userId = UUID.randomUUID();
            var userModel = new UserModel();
            userModel.setUserId(userId);
            userModel.setUserStatus("ACTIVE");

            var addressModel = new AddressModel();

            addressModel.setAddressId(UUID.randomUUID());
            addressModel.setStreet("123 Main St");
            addressModel.setCity("New York");
            addressModel.setState("NY");
            addressModel.setPostalCode("12663521");
            addressModel.setCountry("USA");
            addressModel.setUser(userModel);

            when(addressRepository.findById(addressModel.getAddressId())).thenReturn(Optional.of(addressModel));
            when(permissionUtils.hasPermission(userModel.getUserId())).thenReturn(true);
            when(addressRepository.countByUserUserIdAndAddressStatus(addressModel.getUserId(),AddressStatus.ACTIVE)).thenReturn(2L);

            ResponseEntity<Object> response = addressService.deactivateAddress(addressModel.getAddressId());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        }

    }

    @Nested
    @DisplayName("Tests for updateAddress")
    class UpdateAddressTests {

        @Test
        @DisplayName("Should return FORBIDDEN when user does not have permission to update address")
        void shouldReturnForbiddenWhenUserDoesNotHavePermissionToUpdateAddress() {
            UUID userId = UUID.randomUUID();
            UUID addressId = UUID.randomUUID();

            var userModel = new UserModel();
            userModel.setUserId(userId);

            var addressModel = new AddressModel();
            addressModel.setAddressId(addressId);
            addressModel.setUser(userModel);

            var addressDto = new AddressDto();
            addressDto.setStreet("New Street");
            addressDto.setCity("New City");
            addressDto.setState("New State");
            addressDto.setPostalCode("00000000");
            addressDto.setCountry("New Country");

            when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressModel));
            when(permissionUtils.hasPermission(userId)).thenReturn(false);

            ResponseEntity<Object> response = addressService.updateAddress(addressId, addressDto);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isEqualTo("You do not have permission to update this address.");
        }

        @Test
        @DisplayName("Should update address successfully when user has permission")
        void shouldUpdateAddressSuccessfully() {
            UUID userId = UUID.randomUUID();
            UUID addressId = UUID.randomUUID();

            var userModel = new UserModel();
            userModel.setUserId(userId);

            var addressModel = new AddressModel();
            addressModel.setAddressId(addressId);
            addressModel.setUser(userModel);
            addressModel.setStreet("Old Street");
            addressModel.setCity("Old City");
            addressModel.setState("Old State");
            addressModel.setPostalCode("11111111");
            addressModel.setCountry("Old Country");
            addressModel.setAddressStatus(AddressStatus.ACTIVE);

            var addressDto = new AddressDto();
            addressDto.setStreet("New Street");
            addressDto.setCity("New City");
            addressDto.setState("New State");
            addressDto.setPostalCode("00000000");
            addressDto.setCountry("New Country");

            when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressModel));
            when(permissionUtils.hasPermission(userId)).thenReturn(true);
            when(addressRepository.save(any(AddressModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ResponseEntity<Object> response = addressService.updateAddress(addressId, addressDto);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(AddressModel.class);

            AddressModel updatedAddress = (AddressModel) response.getBody();
            assertThat(updatedAddress.getStreet()).isEqualTo("New Street");
            assertThat(updatedAddress.getCity()).isEqualTo("New City");
            assertThat(updatedAddress.getState()).isEqualTo("New State");
            assertThat(updatedAddress.getPostalCode()).isEqualTo("00000000");
            assertThat(updatedAddress.getCountry()).isEqualTo("New Country");
        }
    }

    @Nested
    @DisplayName("Tests for getUserAddresses")
    class GetUserAddressesTests {

        @Test
        @DisplayName("Should return FORBIDDEN when user does not have permission to view addresses")
        void shouldReturnForbiddenWhenUserDoesNotHavePermissionToViewAddresses() {
            UUID userId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 10);

            when(permissionUtils.hasPermission(userId)).thenReturn(false);

            ResponseEntity<Page<AddressModel>> response = addressService.getUserAddresses(userId, pageable);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNull();
        }

        @Test
        @DisplayName("Should return a page of active addresses when user has permission")
        void shouldReturnPageOfActiveAddressesWhenUserHasPermission() {
            UUID userId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 10);

            AddressModel address1 = new AddressModel();
            address1.setAddressId(UUID.randomUUID());
            address1.setStreet("Rua A");
            address1.setAddressStatus(AddressStatus.ACTIVE);

            AddressModel address2 = new AddressModel();
            address2.setAddressId(UUID.randomUUID());
            address2.setStreet("Rua B");
            address2.setAddressStatus(AddressStatus.ACTIVE);

            List<AddressModel> addressList = List.of(address1, address2);
            Page<AddressModel> addressPage = new PageImpl<>(addressList, pageable, addressList.size());

            when(permissionUtils.hasPermission(userId)).thenReturn(true);
            when(addressRepository.findByUserUserIdAndAddressStatus(userId, AddressStatus.ACTIVE, pageable)).thenReturn(addressPage);

            ResponseEntity<Page<AddressModel>> response = addressService.getUserAddresses(userId, pageable);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getContent()).hasSize(2);
            assertThat(response.getBody().getContent()).containsExactly(address1, address2);
        }
    }
}
