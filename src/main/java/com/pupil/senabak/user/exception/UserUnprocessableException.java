package com.pupil.senabak.user.exception;

public class UserUnprocessableException extends RuntimeException{
    public UserUnprocessableException(String message) {
        super(message);
    }
}
