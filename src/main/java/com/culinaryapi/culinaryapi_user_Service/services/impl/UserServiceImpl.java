package com.culinaryapi.culinaryapi_user_Service.services.impl;

import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import com.culinaryapi.culinaryapi_user_Service.repositories.UserRepository;
import com.culinaryapi.culinaryapi_user_Service.services.UserService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private  final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Object> getUserWithAddresses(UUID userId) {
        Optional<UserModel> userModelOptional = userRepository.findById(userId);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
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
