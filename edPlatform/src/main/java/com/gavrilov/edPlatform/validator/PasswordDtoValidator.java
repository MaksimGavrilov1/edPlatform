package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
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
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "Empty.passwordDto.currentPassword", PlatformValidationUtilities.NOT_EMPTY_COURSE_NAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "Empty.passwordDto.newPassword", PlatformValidationUtilities.NOT_EMPTY_COURSE_DESCRIPTION);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "Empty.passwordDto.confirmPassword", PlatformValidationUtilities.NOT_EMPTY_COURSE_DESCRIPTION);

        if (!errors.hasErrors()) {
            if (!encoder.matches(formPassword.getCurrentPassword().trim(), user.getPassword())) {
                errors.rejectValue("currentPassword", "PasswordMismatch.passwordDto.currentPassword", PlatformValidationUtilities.USER_CURRENT_PASSWORD_MISMATCH);
            }
            if (!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("confirmPassword")) {
                if (formPassword.getNewPassword().contains(" ")) {
                    errors.rejectValue("newPassword", "ForbiddenSymbol.passwordDto.newPassword", PlatformValidationUtilities.FORBIDDEN_SYMBOL_WHITESPACE_PASSWORD);
                }
                if (formPassword.getNewPassword().length() < PlatformValidationUtilities.MIN_PASSWORD_SIZE) {
                    errors.rejectValue("newPassword", "Length.passwordDto.newPassword", PlatformValidationUtilities.LENGTH_PASSWORD);
                }
                if (!formPassword.getNewPassword().trim().equals(formPassword.getConfirmPassword().trim())) {
                    errors.rejectValue("confirmPassword", "PasswordMismatch.passwordDto.confirmPassword", PlatformValidationUtilities.USER_PASSWORD_MISMATCH);
                }
            }
            if (encoder.matches(formPassword.getNewPassword().trim(), user.getPassword())) {
                errors.rejectValue("newPassword", "PasswordMatch.passwordDto.newPassword", PlatformValidationUtilities.USER_NEW_CURRENT_PASSWORD_MATCH);
            }

        }

    }
}
