package com.gavrilov.edPlatform.exception;

public class CourseSubmitException extends RuntimeException{

    public CourseSubmitException() {
    }

    public CourseSubmitException(String message) {
        super(message);
    }
}
