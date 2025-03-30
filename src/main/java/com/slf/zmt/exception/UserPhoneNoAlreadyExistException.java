package com.slf.zmt.exception;

public class UserPhoneNoAlreadyExistException extends RuntimeException{

    private final String message;

    public UserPhoneNoAlreadyExistException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
