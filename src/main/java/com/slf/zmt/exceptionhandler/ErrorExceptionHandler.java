package com.slf.zmt.exceptionhandler;

import com.slf.zmt.exception.MenuItemNotFoundException;
import com.slf.zmt.exception.RestaurantNotFoudException;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorExceptionHandler {

    private final ResponseStructureBuilder responseStructureBuilder;

    public ErrorExceptionHandler(ResponseStructureBuilder responseStructureBuilder) {
        this.responseStructureBuilder = responseStructureBuilder;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RestaurantNotFoudException.class)
    public ResponseEntity<ErrorStructure<String>> restaurantNotFoundException(RestaurantNotFoudException e){
        return responseStructureBuilder.errorResponse(HttpStatus.NOT_FOUND,e.getMessage(),"Failed to find the restaurant!!");
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<ErrorStructure<String>> menuItemNotFoundException(MenuItemNotFoundException e){
        return responseStructureBuilder.errorResponse(HttpStatus.NOT_FOUND,e.getMessage(),"MenuItem Not Found Exception!!");
    }
}
