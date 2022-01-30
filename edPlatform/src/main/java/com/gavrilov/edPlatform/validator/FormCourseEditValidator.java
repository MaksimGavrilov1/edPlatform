package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.dto.FormCourse;
import com.gavrilov.edPlatform.dto.TagDto;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.repo.CourseRepository;
import com.gavrilov.edPlatform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FormCourseEditValidator implements Validator {

    private final CourseRepository courseRepository;
    private final TagService tagService;
    private boolean repeatedTagFlag = false;

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
            List<TagDto> tags = course.getTags();

            if (allCourses.stream().anyMatch(x->x.getName().equals(course.getName())
                    && !x.getAuthor().equals(courseAuthor))){
                errors.rejectValue("name", "", PlatformValidationUtilities.DUPLICATE_COURSE_NAME);
            }

            if (course.getDays() > PlatformValidationUtilities.MAX_TEST_ACTIVE_TIME){
                errors.rejectValue("days", "", PlatformValidationUtilities.INCORRECT_TEST_MAX_ACTIVE_TIME);
            } else if (course.getDays() < PlatformValidationUtilities.MIN_TEST_ACTIVE_TIME){
                errors.rejectValue("days", "", PlatformValidationUtilities.INCORRECT_TEST_MIN_ACTIVE_TIME);
            }

            if (course.getIsAlwaysOpen() == null) {
                errors.rejectValue("isAlwaysOpen", "", PlatformValidationUtilities.NOT_EMPTY_COURSE_ALWAYS_OPEN);
            }

            for (TagDto tag : tags){
                if (!tag.getName().isBlank()){
                    List<TagDto> dupTags =  tags.stream()
                            .filter(x->x.equals(tag))
                            .collect(Collectors.toList());
                    if (dupTags.size() > 1  && !repeatedTagFlag){
                        errors.rejectValue("tags", "", PlatformValidationUtilities.DUPLICATE_COURSE_TAGS);
                        repeatedTagFlag = true;
                    }
                    if (tagService.findByName(tag.getName().trim()) == null){
                        errors.rejectValue("tags", "", String.format(PlatformValidationUtilities.TAG_NOT_EXIST_MODIFYING, tag.getName()));
                    }
                }

            }

        }
    }
}
