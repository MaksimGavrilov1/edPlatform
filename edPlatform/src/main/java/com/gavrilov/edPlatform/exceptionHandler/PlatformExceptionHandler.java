package com.gavrilov.edPlatform.exceptionHandler;

import com.gavrilov.edPlatform.exception.*;
import com.gavrilov.edPlatform.model.PlatformUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class PlatformExceptionHandler {

    @ExceptionHandler({AccessTestException.class})
    protected String handleAccessTestEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Ошибка доступа к тесту");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({OutOfAttemptsException.class})
    protected String handleOutOfAttemptsEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Ошибка доступа к тесту");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({CourseExpiredException.class})
    protected String handleCourseExpiredEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Ошибка доступа к тесту");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({CourseEditingException.class})
    protected String handleCourseEditingEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Ошибка редактирования курса");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({RoleChangeException.class})
    protected String handleRoleChangeEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Ошибка смены роли");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({CourseSubmitException.class})
    protected String handleCourseSubmitEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Ошибка отправки курса");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({UnauthorizedUserException.class})
    protected String handleUnauthorizedUserEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Необходимо зарегистрироваться или авторизоваться");
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler({AccessProfileException.class})
    protected String handleAccessProfileEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Недостаточно прав");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerError( @AuthenticationPrincipal PlatformUser user, Model model) {
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "error/500";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String forbiddenError( @AuthenticationPrincipal PlatformUser user, Model model) {
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "error/403";
    }

    @ExceptionHandler({CourseSubscribeException.class})
    protected String handleCourseSubscribeEx(HttpServletRequest req, Model model, RuntimeException ex, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("errorName", "Ошибка подписки на курс");
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

}
