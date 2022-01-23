package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gavrilov.edPlatform.constant.PlatformValidationUtilities.isNotValidString;

@Component
@RequiredArgsConstructor
public class UserDtoValidator implements Validator {

    private final int MIN_USERNAME_SIZE = 8;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserDto.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto user = (UserDto) target;

        // Check the fields of platformUser.
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.platformUser.username", PlatformValidationUtilities.NOT_EMPTY_USERNAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.platformUser.password", PlatformValidationUtilities.NOT_EMPTY_USERNAME_PASSWORD);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.platformUser.confirmPassword", PlatformValidationUtilities.NOT_EMPTY_USERNAME_PASSWORD_CONFIRM);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.platformUser.name", PlatformValidationUtilities.NOT_EMPTY_USER_NAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "NotEmpty.platformUser.surname", PlatformValidationUtilities.NOT_EMPTY_USER_SURNAME);



        //validate if fields are not empty
        if (!errors.hasFieldErrors("username")) {
            if (userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "Duplicate.platformUser.username", PlatformValidationUtilities.DUPLICATE_USERNAME);


            } else if (user.getUsername().length() < MIN_USERNAME_SIZE) {
                errors.rejectValue("username", "Size.platformUser.username", PlatformValidationUtilities.LENGTH_USERNAME);
            }
            if (isNotValidString(user.getUsername(), PlatformValidationUtilities.ENG_AND_NUMBERS_PATTERN)) {
                errors.rejectValue("username", "ForbiddenSymbol.platformUser.username", PlatformValidationUtilities.FORBIDDEN_SYMBOL_USERNAME);
            }
        }
        if (!errors.hasFieldErrors("name")){
            if (isNotValidString(user.getName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)){
                errors.rejectValue("name", "ForbiddenSymbol.platformUser.name", PlatformValidationUtilities.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("surname")){
            if (isNotValidString(user.getSurname(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)){
                errors.rejectValue("surname", "ForbiddenSymbol.platformUser.surname", PlatformValidationUtilities.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("middleName")){
            if (!user.getMiddleName().isBlank()){
                if (isNotValidString(user.getMiddleName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)){
                    errors.rejectValue("middleName", "ForbiddenSymbol.platformUser.middleName", PlatformValidationUtilities.FORBIDDEN_SYMBOL_USER_INITIALS);
                }
            }

        }
        if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("confirmPassword")){
            if (user.getPassword().contains(" ")){
                errors.rejectValue("password", "ForbiddenSymbol.platformUser.password", PlatformValidationUtilities.FORBIDDEN_SYMBOL_WHITESPACE_PASSWORD);
            }
            if (user.getPassword().length() < PlatformValidationUtilities.MIN_PASSWORD_SIZE){
                errors.rejectValue("password", "Length.platformUser.password", PlatformValidationUtilities.LENGTH_PASSWORD);
            }
            if (!user.getPassword().trim().equals(user.getConfirmPassword().trim())){
                errors.rejectValue("confirmPassword", "PasswordMismatch.platformUser.confirmPassword", PlatformValidationUtilities.USER_PASSWORD_MISMATCH);
            }
        }
    }



}
