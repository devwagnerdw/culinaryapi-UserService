package com.culinaryapi.culinaryapi_user_Service.services;


import com.culinaryapi.culinaryapi_user_Service.model.UserModel;

import java.util.UUID;

public interface UserService {

    void save(UserModel userModel);

    void delete(UUID userId);
}
