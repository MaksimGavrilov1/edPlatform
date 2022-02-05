package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gavrilov.edPlatform.constant.PlatformValidationUtilities.MIN_USERNAME_SIZE;
import static com.gavrilov.edPlatform.constant.PlatformValidationUtilities.isNotValidString;

@Component
@RequiredArgsConstructor
public class UserDtoValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserDto.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto user = (UserDto) target;

        // Check the fields of platformUser.
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "platformUser.username.empty");
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "platformUser.password.empty");
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "passwordDto.confirmPassword.empty");
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "platformUserProfile.name.empty");
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "platformUserProfile.surname.empty");

        //validate if fields are not empty
        if (!errors.hasFieldErrors("username")) {
            if (userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "platformUser.username.nonUnique");


            } else if (user.getUsername().length() < MIN_USERNAME_SIZE) {
                errors.rejectValue("username", "platformUser.username.length");
            }
            if (isNotValidString(user.getUsername(), PlatformValidationUtilities.ENG_AND_NUMBERS_PATTERN)) {
                errors.rejectValue("username", "platformUser.username.forbiddenSymbol");
            }
        }
        if (!errors.hasFieldErrors("name")) {
            if (isNotValidString(user.getName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)) {
                errors.rejectValue("name", "platformUserProfile.initials.forbiddenSymbol");
            }
        }
        if (!errors.hasFieldErrors("surname")) {
            if (isNotValidString(user.getSurname(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)) {
                errors.rejectValue("surname", "platformUserProfile.initials.forbiddenSymbol");
            }
        }
        if (!errors.hasFieldErrors("middleName")) {
            if (!user.getMiddleName().isBlank()) {
                if (isNotValidString(user.getMiddleName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)) {
                    errors.rejectValue("middleName", "platformUserProfile.initials.forbiddenSymbol");
                }
            }

        }
        if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("confirmPassword")) {
            if (user.getPassword().contains(" ")) {
                errors.rejectValue("password", "passwordDto.newPassword.forbiddenSymbol");
            }
            if (user.getPassword().length() < PlatformValidationUtilities.MIN_PASSWORD_SIZE) {
                errors.rejectValue("password", "passwordDto.newPassword.length");
            }
            if (!user.getPassword().trim().equals(user.getConfirmPassword().trim())) {
                errors.rejectValue("confirmPassword", "passwordDto.confirmPassword.passwordMismatch");
            }
        }
    }


}
