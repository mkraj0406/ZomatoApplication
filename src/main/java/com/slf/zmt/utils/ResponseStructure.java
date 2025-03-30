package com.slf.zmt.utils;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseStructure<T> {
    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseStructure<T> createResponse(HttpStatus status, String message, T data){
        ResponseStructure<T> responseStructure = new ResponseStructure<T>();
        responseStructure.setStatus(status.value());
        responseStructure.setMessage(message);
        responseStructure.setData(data);

        return responseStructure;
    }



}
