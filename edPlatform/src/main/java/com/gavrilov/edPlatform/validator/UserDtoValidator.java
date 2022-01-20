package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.ValidationUtils;
import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gavrilov.edPlatform.constant.ValidationUtils.isNotValidString;

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
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.platformUser.username", ValidationUtils.NOT_EMPTY_USERNAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.platformUser.password", ValidationUtils.NOT_EMPTY_USERNAME_PASSWORD);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.platformUser.confirmPassword", ValidationUtils.NOT_EMPTY_USERNAME_PASSWORD_CONFIRM);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.platformUser.name", ValidationUtils.NOT_EMPTY_USER_NAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "NotEmpty.platformUser.surname", ValidationUtils.NOT_EMPTY_USER_SURNAME);



        //validate if fields are not empty
        if (!errors.hasFieldErrors("username")) {
            if (userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "Duplicate.platformUser.username", ValidationUtils.DUPLICATE_USERNAME);


            } else if (user.getUsername().length() < MIN_USERNAME_SIZE) {
                errors.rejectValue("username", "Size.platformUser.username", ValidationUtils.LENGTH_USERNAME);
            }
            if (isNotValidString(user.getUsername(), ValidationUtils.ENG_AND_NUMBERS_PATTERN)) {
                errors.rejectValue("username", "ForbiddenSymbol.platformUser.username", ValidationUtils.FORBIDDEN_SYMBOL_USERNAME);
            }
        }
        if (!errors.hasFieldErrors("name")){
            if (isNotValidString(user.getName(), ValidationUtils.RU_AND_ENG_PATTERN)){
                errors.rejectValue("name", "ForbiddenSymbol.platformUser.name", ValidationUtils.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("surname")){
            if (isNotValidString(user.getSurname(), ValidationUtils.RU_AND_ENG_PATTERN)){
                errors.rejectValue("surname", "ForbiddenSymbol.platformUser.surname", ValidationUtils.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("middleName")){
            if (!user.getMiddleName().isBlank()){
                if (isNotValidString(user.getMiddleName(), ValidationUtils.RU_AND_ENG_PATTERN)){
                    errors.rejectValue("middleName", "ForbiddenSymbol.platformUser.middleName", ValidationUtils.FORBIDDEN_SYMBOL_USER_INITIALS);
                }
            }

        }
        if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("confirmPassword")){
            if (user.getPassword().contains(" ")){
                errors.rejectValue("password", "ForbiddenSymbol.platformUser.password", ValidationUtils.FORBIDDEN_SYMBOL_WHITESPACE_PASSWORD);
            }
            if (user.getPassword().length() < ValidationUtils.MIN_PASSWORD_SIZE){
                errors.rejectValue("password", "Length.platformUser.password", ValidationUtils.LENGTH_PASSWORD);
            }
            if (!user.getPassword().trim().equals(user.getConfirmPassword().trim())){
                errors.rejectValue("confirmPassword", "PasswordMismatch.platformUser.confirmPassword", ValidationUtils.USER_PASSWORD_MISMATCH);
            }
        }
    }



}
