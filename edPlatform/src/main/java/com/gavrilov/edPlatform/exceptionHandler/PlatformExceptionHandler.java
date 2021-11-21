package com.gavrilov.edPlatform.exceptionHandler;

import com.gavrilov.edPlatform.exception.InvalidTokenException;
import com.gavrilov.edPlatform.exception.UserNotFoundException;
import com.gavrilov.edPlatform.exception.UserProfileNotValidException;
import com.gavrilov.edPlatform.model.error.ApiError;
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
