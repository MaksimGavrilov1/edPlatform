package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.model.CourseTest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ThemeTestValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CourseTest.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "name",
                "",
                PlatformValidationUtilities.NOT_EMPTY_THEME_TEST_NAME);


    }
}
