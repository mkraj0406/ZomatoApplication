package com.slf.zmt.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorStructure <T>{
    private int status;
    private String message;
    private String rootCause;

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static <T> ErrorStructure<T> createErrorStructure(HttpStatus status,String message,String rootCause){
        ErrorStructure<T> errorStructure = new ErrorStructure<T>();
        errorStructure.setStatus(status.value());
        errorStructure.setMessage(message);
        errorStructure.setRootCause(rootCause);

        return errorStructure;
    }
}
