package com.culinaryapi.culinaryapi_user_Service.exception;

public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}