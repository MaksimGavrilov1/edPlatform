package com.gavrilov.edPlatform.validator;


import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.model.TestQuestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Component
public class CourseTestValidator implements Validator {

    @Value("${app.max.test.attempts}")
    private Integer maxTestAttempts;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CourseTest.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseTest courseTest = (CourseTest) target;
        int rightAnswerIter;

        rejectIfEmptyOrWhitespace(errors, "name", "test.name.empty");
        rejectIfEmptyOrWhitespace(errors, "amountOfAttempts", "test.amountOfAttempts.empty");

        if (courseTest.getAmountOfAttempts() < PlatformValidationUtilities.MIN_TEST_ATTEMPT_AMOUNT || courseTest.getAmountOfAttempts() > maxTestAttempts) {
            errors.rejectValue("amountOfAttempts", "test.amountOfAttempts.invalid");
        }

        //validate questions
        for (int i = 0; i < courseTest.getTestQuestions().size(); i++) {
            TestQuestion question = courseTest.getTestQuestions().get(i);
            if (question.getText().trim().isBlank()) {
                errors.rejectValue("testQuestions", "", String.format(PlatformValidationUtilities.NOT_EMPTY_QUESTION_TEXT_MODIFYING, i + 1));
            }
            rightAnswerIter = 0;

            //variable for repeating answers
            List<QuestionStandardAnswer> doubleAnswers = new ArrayList<>(question.getQuestionStandardAnswers());
            doubleAnswers.forEach(x -> x.setText(x.getText().trim()));
            //validate answers
            for (int j = 0; j < question.getQuestionStandardAnswers().size(); j++) {

                QuestionStandardAnswer answer = question.getQuestionStandardAnswers().get(j);
                answer.setText(answer.getText().trim());


                //validate if empty
                if (answer.getText().isBlank()) {
                    errors.rejectValue("testQuestions", "", String.format(PlatformValidationUtilities.NOT_EMPTY_ANSWER_TEXT_MODIFYING, j + 1, i + 1));

                    //check if list with repeatable answer contains an answer
                } else if (doubleAnswers.contains(answer)) {
                    //remove it from list
                    doubleAnswers.remove(answer);
                    //find repeatable answers
                    List<QuestionStandardAnswer> repeatedAnswers = doubleAnswers.stream().filter(x -> x.getText().equals(answer.getText())).collect(Collectors.toList());
                    //remove every repeatable answer with same text
                    if (repeatedAnswers.size() != 0) {
                        errors.rejectValue("testQuestions", "", String.format(PlatformValidationUtilities.REPEATABLE_ANSWER_MODIFYING, i + 1, j + 1, repeatedAnswers.size()));
                        repeatedAnswers.forEach(doubleAnswers::remove);
                    }
                }
                //check if no right answers
                if (answer.getRight()) {
                    rightAnswerIter++;
                }
            }

            //validate if no right answers in one question
            if (rightAnswerIter == 0) {
                errors.rejectValue("testQuestions", "", String.format(PlatformValidationUtilities.NO_RIGHT_ANSWER_MODIFYING, i + 1));
            }
        }
    }
}
