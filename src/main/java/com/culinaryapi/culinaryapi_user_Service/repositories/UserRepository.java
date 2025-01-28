package com.culinaryapi.culinaryapi_user_Service.repositories;

import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel,UUID> {

}
