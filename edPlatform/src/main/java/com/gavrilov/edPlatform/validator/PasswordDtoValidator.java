package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.ValidationUtils;
import com.gavrilov.edPlatform.dto.PasswordDto;
import com.gavrilov.edPlatform.model.PlatformUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PasswordDtoValidator implements Validator {

    private final BCryptPasswordEncoder encoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == PasswordDto.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordDto formPassword = (PasswordDto) target;
        PlatformUser user = formPassword.getUser();
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "Empty.passwordDto.currentPassword", ValidationUtils.NOT_EMPTY_COURSE_NAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "Empty.passwordDto.newPassword", ValidationUtils.NOT_EMPTY_COURSE_DESCRIPTION);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "Empty.passwordDto.confirmPassword", ValidationUtils.NOT_EMPTY_COURSE_DESCRIPTION);

        if (!errors.hasFieldErrors("currentPassword")){
            if (!encoder.matches(formPassword.getCurrentPassword().trim(), user.getPassword())){
                errors.rejectValue("currentPassword", "PasswordMismatch.passwordDto.currentPassword", ValidationUtils.USER_CURRENT_PASSWORD_MISMATCH);
            }
        }
        if (!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("confirmPassword")){
            if (formPassword.getNewPassword().contains(" ")){
                errors.rejectValue("newPassword", "ForbiddenSymbol.passwordDto.newPassword", ValidationUtils.FORBIDDEN_SYMBOL_WHITESPACE_PASSWORD);
            }
            if (formPassword.getNewPassword().length() < ValidationUtils.MIN_PASSWORD_SIZE){
                errors.rejectValue("newPassword", "Length.passwordDto.newPassword", ValidationUtils.LENGTH_PASSWORD);
            }
            if (!formPassword.getNewPassword().trim().equals(formPassword.getConfirmPassword().trim())){
                errors.rejectValue("confirmPassword", "PasswordMismatch.passwordDto.confirmPassword", ValidationUtils.USER_PASSWORD_MISMATCH);
            }
        }
    }
}
