package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.ValidationConstants;
import com.gavrilov.edPlatform.model.CourseTest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ThemeTestValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CourseTest.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "name",
                "",
                ValidationConstants.NOT_EMPTY_THEME_TEST_NAME);


    }
}
