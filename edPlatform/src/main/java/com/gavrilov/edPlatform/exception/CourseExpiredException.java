package com.gavrilov.edPlatform.exception;

public class CourseExpiredException extends RuntimeException{
    public CourseExpiredException(String message) {
        super(message);
    }
}
