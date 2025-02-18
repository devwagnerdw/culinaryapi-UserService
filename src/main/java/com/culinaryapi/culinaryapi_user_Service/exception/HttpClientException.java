package com.culinaryapi.culinaryapi_user_Service.exception;

public class HttpClientException extends RuntimeException{
    public HttpClientException(String message) {
        super(message);
    }
}