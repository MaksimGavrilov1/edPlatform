package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gavrilov.edPlatform.constant.PlatformValidationUtilities.isNotValidString;

@Component
public class PlatformUserProfileValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == PlatformUserProfile.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PlatformUserProfile profile = (PlatformUserProfile) target;

        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "platformUserProfile.name.empty");
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "platformUserProfile.surname.empty");

        if (!errors.hasFieldErrors("name")) {
            if (isNotValidString(profile.getName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)) {
                errors.rejectValue("name", "platformUserProfile.initials.forbiddenSymbol");
            }
        }
        if (!errors.hasFieldErrors("surname")) {
            if (isNotValidString(profile.getSurname(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)) {
                errors.rejectValue("surname", "platformUserProfile.initials.forbiddenSymbol");
            }
        }
        if (!errors.hasFieldErrors("middleName")) {
            if (!profile.getMiddleName().isBlank()) {
                if (isNotValidString(profile.getMiddleName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)) {
                    errors.rejectValue("middleName", "platformUserProfile.initials.forbiddenSymbol");
                }
            }
        }
    }
}
