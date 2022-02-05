package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.dto.AnswerDto;
import com.gavrilov.edPlatform.dto.QuestionDto;
import com.gavrilov.edPlatform.dto.TestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TestDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == TestDto.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        TestDto test = (TestDto) target;
        boolean answerFlag;

        for (int i=0;i<test.getQuestions().size();i++){
            QuestionDto question = test.getQuestions().get(i);
            answerFlag = false;
            for (AnswerDto answer : question.getAnswers()){
                if (answer.getChecked()) {
                    answerFlag = true;
                    break;
                }
            }
            if (!answerFlag) {
                errors.rejectValue("questions", "test.answer.noChosen", new Object[]{i+1}, "");
            }
        }
    }
}
