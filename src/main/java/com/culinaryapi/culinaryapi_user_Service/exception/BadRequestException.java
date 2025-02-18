package com.culinaryapi.culinaryapi_user_Service.exception;


public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}