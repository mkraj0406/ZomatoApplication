package com.slf.zmt.exception;

public class OrderItemNotFoundException extends RuntimeException{
    private final String message;

    public OrderItemNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
