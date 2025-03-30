package com.slf.zmt.utils;


import com.slf.zmt.exceptionhandler.ErrorStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseStructureBuilder {

    public  <T> ResponseEntity<ResponseStructure<T>> succesResponse(HttpStatus status,String message, T data){
        return  ResponseEntity.status(status).body(ResponseStructure.createResponse(status, message, data));
    }

    public <T> ResponseEntity<ErrorStructure<T>> errorResponse(HttpStatus status,String message,String rootCause){
        return ResponseEntity.status(status).body(ErrorStructure.createErrorStructure(status, message, rootCause));
    }

}
