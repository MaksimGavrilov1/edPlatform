package com.gavrilov.edPlatform.service.themeTestServiceImpl;

import com.gavrilov.edPlatform.dto.*;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.model.TestQuestion;
import com.gavrilov.edPlatform.model.enumerator.AnswerStatus;
import com.gavrilov.edPlatform.repo.ThemeTestRepository;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.QuestionStandardAnswerService;
import com.gavrilov.edPlatform.service.TestQuestionService;
import com.gavrilov.edPlatform.service.ThemeTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeTestServiceImpl implements ThemeTestService {

    private final ThemeTestRepository themeTestRepository;
    private final TestQuestionService testQuestionService;
    private final QuestionStandardAnswerService questionStandardAnswerService;
    private final CourseService courseService;

    @Override
    public CourseTest initSave(CourseTest themeTest) {
        return themeTestRepository.save(themeTest);
    }

    @Override
    public CourseTest findTest(Long id) {
        return themeTestRepository.findById(id).orElse(null);
    }

    @Override
    public TestResultDto calculateResult(TestDto source) {
        CourseTest testFromDB = courseService.findCourse(source.getCourseId()).getTest();
        TestResultDto result = new TestResultDto();
        result.setName(source.getName());
        List<TestQuestion> questionsFromDB = testFromDB.getTestQuestions();
        List<QuestionDto> questionsFromSource = source.getQuestions();
        int mark = 0;

        for (int i = 0; i < questionsFromDB.size(); i++) {

            TestQuestion questionFromDB = questionsFromDB.get(i);
            QuestionDto questionFromSource = questionsFromSource.get(i);
            QuestionResultDto questionResult = new QuestionResultDto();
            questionResult.setId(questionFromSource.getId());
            questionResult.setTitle(questionFromSource.getTitle());
            questionResult.setRightAnswerAmount(questionFromSource.getRightAnswerAmount());
            result.getQuestions().add(questionResult);
            Integer rightAnswerIter = 0;

            for (QuestionStandardAnswer answerFromDB : questionFromDB.getQuestionStandardAnswers()) {

                AnswerResultDto answerResult = new AnswerResultDto();
                answerResult.setQuestionId(questionResult.getId());
                answerResult.setText(answerFromDB.getText());
                questionResult.getAnswers().add(answerResult);

                for (AnswerDto answerFromSource : questionFromSource.getAnswers()) {

                    if (answerFromDB.getId().equals(answerFromSource.getId())) {

                        if (answerFromDB.getIsRight() && answerFromSource.getChecked()) {
                            answerResult.setStatus(AnswerStatus.CHOSEN_RIGHT);
                            rightAnswerIter++;
                        }
                        if (!answerFromDB.getIsRight() && answerFromSource.getChecked()) {
                            answerResult.setStatus(AnswerStatus.CHOSEN_WRONG);
                        }
                    }
                }
            }

            if (rightAnswerIter.equals(questionFromSource.getRightAnswerAmount())) {
                mark++;
            }
        }
        result.setMark(mark);
        return result;
    }

    @Override
    @Transactional
    public CourseTest save(CourseTest courseTest) {

        CourseTest courseTestFromDB = themeTestRepository.getById(courseTest.getId());


        courseTestFromDB.setTestQuestions(courseTest.getTestQuestions());
        themeTestRepository.save(courseTestFromDB);

        for (TestQuestion question :
                courseTest.getTestQuestions()) {
            testQuestionService.save(question);
//            for (QuestionStandardAnswer answer: question.getQuestionStandardAnswers()){
//                questionStandardAnswerService.save(answer);
//            }
        }

        return courseTestFromDB;
    }
}
