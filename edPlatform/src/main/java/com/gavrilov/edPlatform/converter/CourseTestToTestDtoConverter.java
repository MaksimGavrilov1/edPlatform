package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.AnswerDto;
import com.gavrilov.edPlatform.dto.QuestionDto;
import com.gavrilov.edPlatform.dto.TestDto;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.model.TestQuestion;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

public class CourseTestToTestDtoConverter implements Converter<CourseTest, TestDto> {

    @Override
    public TestDto convert(CourseTest source) {
        TestDto test = new TestDto();

        //fill test dto with course id and name
        test.setCourseId(source.getCourse().getId());
        test.setName(source.getName());

        //fill questions from source
        for (TestQuestion question :
                source.getTestQuestions()) {
            QuestionDto questionDto = new QuestionDto();
            questionDto.setId(question.getId());
            questionDto.setTitle(question.getText());
            List<QuestionStandardAnswer> questionAnswers = question.getQuestionStandardAnswers();

            //variable for calculating the right answer amount at one question
            int rightAnswerAmount = 0;

            for (QuestionStandardAnswer answer : questionAnswers) {

                //fill answer dto from source
                AnswerDto answerDto = new AnswerDto();
                answerDto.setId(answer.getId());
                answerDto.setTitle(answer.getText());
                answerDto.setQuestionId(question.getId());

                //increment amount of right answers if this answer from source is correct
                if (answer.getRight()) {
                    rightAnswerAmount++;
                }
                questionDto.getAnswers().add(answerDto);
            }
            questionDto.setRightAnswerAmount(rightAnswerAmount);
            test.getQuestions().add(questionDto);
        }
        return test;
    }
}
