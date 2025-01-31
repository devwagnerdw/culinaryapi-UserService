package com.culinaryapi.culinaryapi_user_Service.services;


import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void save(UserModel userModel);

    void delete(UUID userId);
}
