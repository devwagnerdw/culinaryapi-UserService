package com.culinaryapi.culinaryapi_user_Service.repositories;

import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<AddressModel, UUID> {

}
