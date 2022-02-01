package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.dto.FormTest;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Component
@RequiredArgsConstructor
public class FormTestValidator implements Validator {

    private final CourseService courseService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == FormTest.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        FormTest test = (FormTest) target;
        Course course = courseService.findCourse(test.getCourseId());

        rejectIfEmptyOrWhitespace(errors, "name", "test.name.empty");
        rejectIfEmptyOrWhitespace(errors, "questionAmount", "test.questionAmount.empty");
        rejectIfEmptyOrWhitespace(errors, "amountOfAttempts", "test.amountOfAttempts.empty");

        if (!errors.hasErrors()) {
            if (test.getCourseId() == null) {
                errors.rejectValue("courseId", "test.courseId.empty");
            }
            if (test.getQuestionAmount() < PlatformValidationUtilities.MIN_TEST_QUESTION_AMOUNT || test.getQuestionAmount() > PlatformValidationUtilities.MAX_TEST_QUESTION_AMOUNT) {
                errors.rejectValue("questionAmount","test.questionAmount.invalid");
            }
            if (test.getAmountOfAttempts() < PlatformValidationUtilities.MIN_TEST_ATTEMPT_AMOUNT || test.getAmountOfAttempts() > PlatformValidationUtilities.MAX_TEST_ATTEMPT_AMOUNT) {
                errors.rejectValue("amountOfAttempts","test.amountOfAttempts.invalid");
            }
            if (course.getTest() != null){
                errors.rejectValue("courseId","test.courseId.nonUnique");
            }
            if (test.getMinThreshold() >= test.getQuestionAmount()) {
                errors.rejectValue("minThreshold", "test.minThreshold.invalid");
            }
            if (test.getMinThreshold() == null){
                int halfQuestions = test.getQuestionAmount() % 2 == 0 ? (test.getQuestionAmount() / 2) : (test.getQuestionAmount() / 2 + 1);
                test.setMinThreshold(halfQuestions);
            }
        }
    }
}
