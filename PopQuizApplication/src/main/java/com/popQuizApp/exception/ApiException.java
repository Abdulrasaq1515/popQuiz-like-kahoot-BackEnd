package com.popQuizApp.exception;

public class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
    }
}
