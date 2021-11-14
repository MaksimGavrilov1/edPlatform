package com.gavrilov.edPlatform.exceptions;

public class UserProfileNotValidException extends RuntimeException {

    public UserProfileNotValidException() {
    }

    public UserProfileNotValidException(String message) {
        super(message);
    }

    public UserProfileNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserProfileNotValidException(Throwable cause) {
        super(cause);
    }

    public UserProfileNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
