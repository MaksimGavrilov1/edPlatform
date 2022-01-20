package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.ValidationUtils;
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
public class CourseValidator implements Validator {

    private final CourseService courseService;
    private final UserService userService;
    private final CourseRepository courseRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Course.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Course course = (Course) target;

        // Check the fields of course.
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", ValidationUtils.NOT_EMPTY_COURSE_NAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "", ValidationUtils.NOT_EMPTY_COURSE_DESCRIPTION);

        //validate if all fields are not empty and check if author have the same course
        if (!errors.hasErrors()) {

            PlatformUser courseAuthor = course.getAuthor();
            List<Course> authorCourses = courseRepository.findCourseByAuthor(courseAuthor);

            if (authorCourses.stream().anyMatch((x) -> x.getName().equals(course.getName()))) {

                errors.rejectValue("name", "", ValidationUtils.DUPLICATE_AUTHOR_COURSE);
            }


        }
    }
}
