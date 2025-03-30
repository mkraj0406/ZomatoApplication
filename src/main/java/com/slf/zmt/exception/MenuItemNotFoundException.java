package com.slf.zmt.exception;

public class MenuItemNotFoundException extends RuntimeException{

    private final String message;

    public MenuItemNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
