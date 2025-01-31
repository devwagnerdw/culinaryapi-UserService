package com.culinaryapi.culinaryapi_user_Service.services.impl;

import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.repositories.UserRepository;
import com.culinaryapi.culinaryapi_user_Service.services.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private  final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void save(UserModel userModel) {
        userRepository.save(userModel);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.deleteById(userId);
    }
}
