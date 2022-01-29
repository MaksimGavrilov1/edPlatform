package com.gavrilov.edPlatform.exception;

public class OutOfAttemptsException extends RuntimeException{
    public OutOfAttemptsException(String message) {
        super(message);
    }
}
