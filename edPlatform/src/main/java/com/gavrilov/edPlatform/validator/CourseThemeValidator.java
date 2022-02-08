package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Component
@RequiredArgsConstructor
public class CourseThemeValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CourseTheme.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseTheme courseTheme = (CourseTheme) target;
        // Check the fields of course theme.
        rejectIfEmptyOrWhitespace(errors, "name", "theme.name.empty");



        if (!errors.hasErrors()) {
            Course course = courseTheme.getCourse();


            if (course.getThemes().stream().anyMatch(x -> x.equals(courseTheme))) {
                errors.rejectValue("name", "theme.name.nonUnique");
            }
        }
    }
}
