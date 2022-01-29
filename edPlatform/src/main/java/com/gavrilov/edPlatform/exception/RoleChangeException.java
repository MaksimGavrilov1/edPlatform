package com.gavrilov.edPlatform.exception;

public class RoleChangeException extends RuntimeException{

    public RoleChangeException() {
    }

    public RoleChangeException(String message) {
        super(message);
    }
}
