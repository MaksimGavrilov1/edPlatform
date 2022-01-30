package com.gavrilov.edPlatform.exception;

public class CourseSubscribeException extends RuntimeException{
    public CourseSubscribeException() {
    }

    public CourseSubscribeException(String message) {
        super(message);
    }

    public CourseSubscribeException(String message, Throwable cause) {
        super(message, cause);
    }
}
