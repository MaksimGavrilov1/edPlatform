package com.gavrilov.edPlatform.service.themeTestServiceImpl;

import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.model.TestQuestion;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.repo.ThemeTestRepository;
import com.gavrilov.edPlatform.service.QuestionStandardAnswerService;
import com.gavrilov.edPlatform.service.TestQuestionService;
import com.gavrilov.edPlatform.service.ThemeTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ThemeTestServiceImpl implements ThemeTestService {

    private final ThemeTestRepository themeTestRepository;
    private final TestQuestionService testQuestionService;
    private final QuestionStandardAnswerService questionStandardAnswerService;

    @Override
    public CourseTest initSave(CourseTest themeTest){
        return themeTestRepository.save(themeTest);
    }

    @Override
    public CourseTest save(CourseTest courseTest) {

        CourseTest courseTestFromDB = themeTestRepository.getById(courseTest.getId());
        courseTestFromDB.setTestQuestions(courseTest.getTestQuestions());
        themeTestRepository.save(courseTestFromDB);

        for (TestQuestion question:
                courseTest.getTestQuestions()) {
            testQuestionService.save(question);
//            for (QuestionStandardAnswer answer: question.getQuestionStandardAnswers()){
//                questionStandardAnswerService.save(answer);
//            }
        }

        return courseTestFromDB;
    }
}
