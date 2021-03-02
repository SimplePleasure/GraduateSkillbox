package com.graduate.exceptionHandler.exceptions;

public class ComponentNotFoundException extends RuntimeException {

    public ComponentNotFoundException(String msg) {
        super(msg);
    }
}
