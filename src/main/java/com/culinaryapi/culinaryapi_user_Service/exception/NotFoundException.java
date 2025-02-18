package com.culinaryapi.culinaryapi_user_Service.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}