package com.gavrilov.edPlatform.exceptionHandler;

import com.gavrilov.edPlatform.exception.*;
import com.gavrilov.edPlatform.model.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class PlatformExceptionHandler {

    @ExceptionHandler({AccessTestException.class})
    protected String handleAccessTestEx(HttpServletRequest req, Model model, RuntimeException ex) {
        model.addAttribute("errorName", "Ошибка доступа к тесту");
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({OutOfAttemptsException.class})
    protected String handleOutOfAttemptsEx(HttpServletRequest req, Model model, RuntimeException ex) {
        model.addAttribute("errorName", "Ошибка доступа к тесту");
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({CourseExpiredException.class})
    protected  String handleCourseExpiredEx(HttpServletRequest req, Model model, RuntimeException ex){
        model.addAttribute("errorName", "Ошибка доступа к тесту");
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({CourseEditingException.class})
    protected  String handleCourseEditingEx(HttpServletRequest req, Model model, RuntimeException ex){
        model.addAttribute("errorName", "Ошибка редактирования курса");
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({RoleChangeException.class})
    protected  String handleRoleChangeEx(HttpServletRequest req, Model model, RuntimeException ex){
        model.addAttribute("errorName", "Ошибка смены роли");
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({CourseSubmitException.class})
    protected  String handleCourseSubmitEx(HttpServletRequest req, Model model, RuntimeException ex){
        model.addAttribute("errorName", "Ошибка отправки курса");
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }


}
