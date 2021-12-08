package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.ValidationConstants;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CourseValidator implements Validator {

    private final CourseService courseService;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Course.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Course course = (Course) target;

        // Check the fields of course.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", ValidationConstants.NOT_EMPTY_COURSE_NAME);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "", ValidationConstants.NOT_EMPTY_COURSE_DESCRIPTION);

        //validate if all fields are not empty and check if author have the same course
        if (!errors.hasErrors()) {
            PlatformUser courseAuthor = course.getAuthor();
            if (courseAuthor == null) {
                System.out.println("AUTHOR IS NULL");
            }
            Set<Course> authorCourses = courseAuthor.getOwnedCourses();
            System.out.println(authorCourses);
            if (authorCourses.stream().anyMatch((x) -> x.getName().equals(course.getName()))) {
                System.out.println("In validator");
                errors.reject("", ValidationConstants.DUPLICATE_AUTHOR_COURSE);
            }


        }
    }
}
