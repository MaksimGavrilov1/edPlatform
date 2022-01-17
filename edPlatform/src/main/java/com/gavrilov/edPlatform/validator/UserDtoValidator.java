package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.ValidationConstants;
import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.platformUser.username", ValidationConstants.NOT_EMPTY_USERNAME);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.platformUser.password", ValidationConstants.NOT_EMPTY_USERNAME_PASSWORD);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.platformUser.confirmPassword", ValidationConstants.NOT_EMPTY_USERNAME_PASSWORD_CONFIRM);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.platformUser.name", ValidationConstants.NOT_EMPTY_USER_NAME);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "NotEmpty.platformUser.surname", ValidationConstants.NOT_EMPTY_USER_SURNAME);



        //validate if fields are not empty
        if (!errors.hasFieldErrors("username")) {
            if (userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "Duplicate.platformUser.username", ValidationConstants.DUPLICATE_USERNAME);


            } else if (user.getUsername().length() < MIN_USERNAME_SIZE) {
                errors.rejectValue("username", "Size.platformUser.username", ValidationConstants.LENGTH_USERNAME);
            }
            if (isNotValidString(user.getUsername(), ValidationConstants.ENG_AND_NUMBERS_PATTERN)) {
                errors.rejectValue("username", "ForbiddenSymbol.platformUser.username", ValidationConstants.FORBIDDEN_SYMBOL_USERNAME);
            }
        }
        if (!errors.hasFieldErrors("name")){
            if (isNotValidString(user.getName(), ValidationConstants.RU_AND_ENG_PATTERN)){
                errors.rejectValue("name", "ForbiddenSymbol.platformUser.name", ValidationConstants.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("surname")){
            if (isNotValidString(user.getSurname(), ValidationConstants.RU_AND_ENG_PATTERN)){
                errors.rejectValue("surname", "ForbiddenSymbol.platformUser.surname", ValidationConstants.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("middleName")){
            if (isNotValidString(user.getMiddleName(), ValidationConstants.RU_AND_ENG_PATTERN)){
                errors.rejectValue("middleName", "ForbiddenSymbol.platformUser.middleName", ValidationConstants.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("selfDescription")){
            if (isNotValidString(user.getSelfDescription(), ValidationConstants.RU_AND_ENG_PATTERN)){
                errors.rejectValue("selfDescription", "ForbiddenSymbol.platformUser.selfDescription", ValidationConstants.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("confirmPassword")){
            if (!user.getPassword().trim().equals(user.getConfirmPassword().trim())){
                errors.rejectValue("confirmPassword", "PasswordMismatch.platformUser.confirmPassword", ValidationConstants.USER_PASSWORD_MISMATCH);
            }
        }
    }

    private boolean isNotValidString(String string, String pattern) {
        Pattern validPattern = Pattern.compile(pattern);
        Matcher matcher = validPattern.matcher(string.trim());
        return !matcher.matches();
    }

}
