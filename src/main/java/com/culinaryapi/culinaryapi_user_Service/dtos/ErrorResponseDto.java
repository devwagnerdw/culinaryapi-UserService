package com.culinaryapi.culinaryapi_user_Service.dtos;

import org.springframework.http.HttpStatus;

public class ErrorResponseDto {
    private String message;
    private HttpStatus httpStatus;
    private Integer statusCode;

    private ErrorResponseDto(Builder builder) {
        this.message = builder.message;
        this.httpStatus = builder.httpStatus;
        this.statusCode = builder.statusCode;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public static class Builder {
        private String message;
        private HttpStatus httpStatus;
        private Integer statusCode;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ErrorResponseDto build() {
            return new ErrorResponseDto(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
