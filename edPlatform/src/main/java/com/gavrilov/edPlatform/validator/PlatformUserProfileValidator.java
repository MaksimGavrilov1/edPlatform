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

        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.platformUserProfile.name", PlatformValidationUtilities.NOT_EMPTY_USER_NAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "NotEmpty.platformUserProfile.surname", PlatformValidationUtilities.NOT_EMPTY_USER_SURNAME);

        if (!errors.hasFieldErrors("name")){
            if (isNotValidString(profile.getName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)){
                errors.rejectValue("name", "ForbiddenSymbol.platformUserProfile.name", PlatformValidationUtilities.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("surname")){
            if (isNotValidString(profile.getSurname(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)){
                errors.rejectValue("surname", "ForbiddenSymbol.platformUserProfile.surname", PlatformValidationUtilities.FORBIDDEN_SYMBOL_USER_INITIALS);
            }
        }
        if (!errors.hasFieldErrors("middleName")){
            if (!profile.getMiddleName().isBlank()){
                if (isNotValidString(profile.getMiddleName(), PlatformValidationUtilities.RU_AND_ENG_PATTERN)){
                    errors.rejectValue("middleName", "ForbiddenSymbol.platformUserProfile.middleName", PlatformValidationUtilities.FORBIDDEN_SYMBOL_USER_INITIALS);
                }
            }
        }
    }
}
