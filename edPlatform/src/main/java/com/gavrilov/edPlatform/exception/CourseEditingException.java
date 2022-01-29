package com.gavrilov.edPlatform.exception;

public class CourseEditingException extends RuntimeException{
    public CourseEditingException() {
    }

    public CourseEditingException(String message) {
        super(message);
    }
}
