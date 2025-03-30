package com.slf.zmt.exception;

public class RestaurantNotFoudException extends RuntimeException {

    private final String message;

    public RestaurantNotFoudException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
