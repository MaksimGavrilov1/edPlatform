package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.dto.FormCourse;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.repo.CourseRepository;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FormCourseValidator implements Validator {

    private final CourseService courseService;
    private final UserService userService;
    private final CourseRepository courseRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == FormCourse.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        FormCourse course = (FormCourse) target;
        course.setName(course.getName().trim());
        // Check the fields of course.
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", PlatformValidationUtilities.NOT_EMPTY_COURSE_NAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "", PlatformValidationUtilities.NOT_EMPTY_COURSE_DESCRIPTION);

        if (course.getName().length() > PlatformValidationUtilities.MAX_COURSE_NAME_SIZE){
            errors.rejectValue("name", "", PlatformValidationUtilities.LENGTH_COURSE_NAME);
        }

        //validate if all fields are not empty and check if author have the same course
        if (!errors.hasErrors()) {

            PlatformUser courseAuthor = course.getAuthor();
            List<Course> authorCourses = courseRepository.findCourseByAuthor(courseAuthor);
            List<Course> allCourses = courseRepository.findAll();

            if (authorCourses.stream().anyMatch((x) -> x.getName().equals(course.getName()))) {

                errors.rejectValue("name", "", PlatformValidationUtilities.DUPLICATE_AUTHOR_COURSE);
            }

            if (allCourses.stream().anyMatch(x->x.getName().equals(course.getName())
                    && !x.getAuthor().equals(courseAuthor))){
                errors.rejectValue("name", "", PlatformValidationUtilities.DUPLICATE_COURSE_NAME);
            }

            if (course.getDays() > PlatformValidationUtilities.MAX_TEST_ACTIVE_TIME){
                errors.rejectValue("days", "", PlatformValidationUtilities.INCORRECT_TEST_MAX_ACTIVE_TIME);
            }

        }
    }
}
