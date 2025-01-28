package com.culinaryapi.culinaryapi_user_Service.dtos;


import com.culinaryapi.culinaryapi_user_Service.model.UserModel;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class UserEventDto {

    private UUID userId;
    private String username;
    private String userStatus;
    private String phoneNumber;
    private String actionType;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public UserModel convertToUserModel(){
        var userModel = new UserModel();
        BeanUtils.copyProperties(this, userModel);
        return userModel;
    }
}



