package com.graduate.exceptionHandler.exceptions;

public class UserNotAuthException extends RuntimeException {

    public UserNotAuthException(String msg) {
        super(msg);
    }

}
