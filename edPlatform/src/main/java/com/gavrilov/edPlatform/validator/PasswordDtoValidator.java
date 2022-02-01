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
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "passwordDto.currentPassword.empty");
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "passwordDto.newPassword.empty");
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "passwordDto.confirmPassword.empty");

        if (!errors.hasErrors()) {
            if (!encoder.matches(formPassword.getCurrentPassword().trim(), user.getPassword())) {
                errors.rejectValue("currentPassword", "passwordDto.currentPassword.passwordMismatch");
            }
            if (!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("confirmPassword")) {
                if (formPassword.getNewPassword().contains(" ")) {
                    errors.rejectValue("newPassword", "passwordDto.newPassword.forbiddenSymbol");
                }
                if (formPassword.getNewPassword().length() < PlatformValidationUtilities.MIN_PASSWORD_SIZE) {
                    errors.rejectValue("newPassword", "passwordDto.newPassword.length");
                }
                if (!formPassword.getNewPassword().trim().equals(formPassword.getConfirmPassword().trim())) {
                    errors.rejectValue("confirmPassword", "passwordDto.confirmPassword.passwordMismatch");
                }
            }
            if (encoder.matches(formPassword.getNewPassword().trim(), user.getPassword())) {
                errors.rejectValue("newPassword", "passwordDto.newPassword.passwordMatch");
            }

        }

    }
}
