package com.carrental.exception;

public class CarOwnerNotFoundException extends RuntimeException {

    public CarOwnerNotFoundException(String message) {
        super(message);
    }
}
