package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.model.CourseTheme;
import com.gavrilov.edPlatform.service.CourseThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseThemeEditValidator implements Validator {

    private final CourseThemeService courseThemeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CourseTheme.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
         CourseTheme theme = (CourseTheme) target;
         CourseTheme themeFromDB = courseThemeService.findTheme(theme.getId());

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", PlatformValidationUtilities.NOT_EMPTY_COURSE_THEME_NAME);

        if (!errors.hasErrors()) {
            List<CourseTheme> themes = courseThemeService.findThemesByCourse(theme.getCourse());
            List<CourseTheme> repeatedThemes = themes.stream()
                    .filter(x->x.getName().equals(theme.getName().trim()))
                    .collect(Collectors.toList());
            for (CourseTheme repeatedTheme : repeatedThemes){
                if (!repeatedTheme.equals(themeFromDB)) {
                    errors.rejectValue("name", "", PlatformValidationUtilities.DUPLICATE_COURSE_THEME);
                }
            }
        }
    }
}
