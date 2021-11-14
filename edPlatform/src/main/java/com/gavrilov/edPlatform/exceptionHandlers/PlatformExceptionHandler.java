package com.gavrilov.edPlatform.exceptionHandlers;

import com.gavrilov.edPlatform.exceptions.InvalidTokenException;
import com.gavrilov.edPlatform.exceptions.UserNotFoundException;
import com.gavrilov.edPlatform.exceptions.UserProfileNotValidException;
import com.gavrilov.edPlatform.models.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class PlatformExceptionHandler {

    @ExceptionHandler({InvalidTokenException.class})
    protected ResponseEntity<Object> handleEntityNotFoundEx(RuntimeException ex, WebRequest request) {
        ApiError apiError = new ApiError("This token is invalid", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundEx(RuntimeException ex, WebRequest request) {
        ApiError apiError = new ApiError("Incorrect authorization data", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected  ResponseEntity<Object> handleConstraintViolationEx(RuntimeException ex, WebRequest request){
        ApiError apiError = new ApiError("Incorrect user profile data", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserProfileNotValidException.class})
    protected  ResponseEntity<Object> handleUserProfileNotValidEx(RuntimeException ex, WebRequest request){
        ApiError apiError = new ApiError("Incorrect user profile data", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
