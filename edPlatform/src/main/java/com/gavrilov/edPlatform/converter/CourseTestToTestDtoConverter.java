package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.AnswerDto;
import com.gavrilov.edPlatform.dto.QuestionDto;
import com.gavrilov.edPlatform.dto.TestDto;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.model.TestQuestion;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            List<QuestionStandardAnswer> questionAnswers = randomizeAnswerOrder(question.getQuestionStandardAnswers());

            //variable for calculating the right answer amount at one question
            int rightAnswerAmount = 0;

            for (QuestionStandardAnswer answer : questionAnswers) {

                //fill answer dto from source
                AnswerDto answerDto = new AnswerDto();
                answerDto.setId(answer.getId());
                answerDto.setTitle(answer.getText());
                answerDto.setQuestionId(question.getId());

                //increment amount of right answers if this answer from source is correct
                if (answer.getIsRight()) {
                    rightAnswerAmount++;
                }
                questionDto.getAnswers().add(answerDto);
            }
            questionDto.setRightAnswerAmount(rightAnswerAmount);
            test.getQuestions().add(questionDto);
        }
        return test;
    }

    private List<QuestionStandardAnswer> randomizeAnswerOrder(List<QuestionStandardAnswer> answers) {

        List<QuestionStandardAnswer> result = new ArrayList<>();
        List<Integer> answerId = new ArrayList<>();
        Random randomNumb = new Random();

        for (int i = 0; i < answers.size(); i++) {
            answerId.add(i);
        }

        for (int i = 0; i < answers.size(); i++) {

            if (i == answers.size() - 1) {
                int rand = randomNumb.nextInt(answerId.size());
                int index = answerId.get(rand);
                result.add(answers.get(index));
                return result;
            }

            //pick random index for answerId array
            int rand = randomNumb.nextInt(answerId.size());

            //random id
            int index = answerId.get(rand);

            //delete used id
            answerId.remove(rand);

            //get answer by id
            result.add(answers.get(index));
        }
        return result;
    }
}
