package com.telemedicicne.telemedicicne.Exception;


public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
