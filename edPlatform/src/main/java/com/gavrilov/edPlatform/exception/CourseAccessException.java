package com.gavrilov.edPlatform.exception;

public class CourseAccessException extends RuntimeException{
    public CourseAccessException() {
    }

    public CourseAccessException(String message) {
        super(message);
    }

    public CourseAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
