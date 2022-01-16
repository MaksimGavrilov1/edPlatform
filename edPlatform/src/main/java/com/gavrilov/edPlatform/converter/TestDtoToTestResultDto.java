package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.*;
import com.gavrilov.edPlatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class TestDtoToTestResultDto implements Converter<TestDto, TestResultDto> {

    private final CourseService courseService;

    @Override
    public TestResultDto convert(TestDto source) {
//        CourseTest testFromDB = courseService.findCourse(source.getCourseId()).getTest();
//        TestResultDto result = new TestResultDto();
//        result.setName(source.getName());
//        List<TestQuestion> questionsFromDB = testFromDB.getTestQuestions();
//        List<QuestionDto> questionsFromSource = source.getQuestions();
//        int mark = 0;
//
//        for (int i = 0; i<questionsFromDB.size(); i++){
//
//            TestQuestion questionFromDB = questionsFromDB.get(i);
//            QuestionDto questionFromSource = questionsFromSource.get(i);
//            QuestionResultDto questionResult = new QuestionResultDto();
//            questionResult.setId(questionFromSource.getId());
//            questionResult.setTitle(questionFromSource.getTitle());
//            questionResult.setRightAnswerAmount(questionFromSource.getRightAnswerAmount());
//            result.getQuestions().add(questionResult);
//            Integer rightAnswerIter = 0;
//
//            for (QuestionStandardAnswer answerFromDB : questionFromDB.getQuestionStandardAnswers()){
//
//                AnswerResultDto answerResult = new AnswerResultDto();
//                answerResult.setQuestionId(questionResult.getId());
//                answerResult.setText(answerFromDB.getText());
//                questionResult.getAnswers().add(answerResult);
//
//                for (AnswerDto answerFromSource : questionFromSource.getAnswers()){
//
//                    if (answerFromDB.getId().equals(answerFromSource.getId())){
//
//                        if (answerFromDB.getIsRight() && answerFromSource.getChecked()){
//                            answerResult.setStatus(AnswerStatus.CHOSEN_RIGHT);
//                            rightAnswerIter++;
//                        }
//                        if (!answerFromDB.getIsRight() && answerFromSource.getChecked()){
//                            answerResult.setStatus(AnswerStatus.CHOSEN_WRONG);
//                        }
//                    }
//                }
//            }
//
//            if (rightAnswerIter.equals(questionFromSource.getRightAnswerAmount())){
//                mark++;
//            }
//        }
//        result.setMark(mark);
//        return result;
        return new TestResultDto();
    }
}
