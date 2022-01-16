package com.gavrilov.edPlatform.service.themeTestServiceImpl;

import com.gavrilov.edPlatform.dto.*;
import com.gavrilov.edPlatform.model.*;
import com.gavrilov.edPlatform.model.enumerator.AnswerStatus;
import com.gavrilov.edPlatform.repo.ThemeTestRepository;
import com.gavrilov.edPlatform.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ThemeTestServiceImpl implements ThemeTestService {

    private final ThemeTestRepository themeTestRepository;
    private final TestQuestionService testQuestionService;
    private final QuestionStandardAnswerService questionStandardAnswerService;
    private final CourseService courseService;
    private final UserAnswerService userAnswerService;
    private final ConversionService conversionService;

    @Override
    public CourseTest initSave(CourseTest themeTest) {
        return themeTestRepository.save(themeTest);
    }

    @Override
    public CourseTest findTest(Long id) {
        return themeTestRepository.findById(id).orElse(null);
    }

    @Override
    public CourseTest randomizeAnswers(CourseTest test) {
        for (TestQuestion question : test.getTestQuestions()){
            List<QuestionStandardAnswer> randomAnswers = randomizeAnswerOrder(question.getQuestionStandardAnswers());
            question.setQuestionStandardAnswers(randomAnswers);
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

    @Override
    public TestResultDto calculateResult(TestDto source, PlatformUser user) {
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

                UserAnswer answerResult = new UserAnswer();
                answerResult.setQuestion(questionFromDB);
                answerResult.setText(answerFromDB.getText());
                questionResult.getAnswers().add(answerResult);

                for (AnswerDto answerFromSource : questionFromSource.getAnswers()) {

                    if (answerFromDB.getId().equals(answerFromSource.getId())) {

                        if (answerFromDB.getRight() && answerFromSource.getChecked()) {
                            answerResult.setStatus(AnswerStatus.CHOSEN_RIGHT);
                            answerResult.setUser(user);
                            userAnswerService.save(answerResult);
                            rightAnswerIter++;
                        }
                        if (!answerFromDB.getRight() && answerFromSource.getChecked()) {
                            answerResult.setStatus(AnswerStatus.CHOSEN_WRONG);
                            answerResult.setUser(user);
                            userAnswerService.save(answerResult);
                            answerResult.setUser(user);
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

    public TestResultDto formResult(List<UserAnswer> answers, Long courseId) {
        Course course = courseService.findCourse(courseId);
        CourseTest test = course.getTest();

        //variable for right answer amount
        TestDto testDto = conversionService.convert(test,TestDto.class);

        TestResultDto result = new TestResultDto();
        result.setName(test.getName());
        int mark = 0;
        Integer rightAnswerIter = 0;
        boolean skipStandardAnswerFlag = false;

        for (int i = 0; i<test.getTestQuestions().size();i++) {

            TestQuestion questionFromDB = test.getTestQuestions().get(i);
            QuestionDto questionWithRightAnswerAmount = testDto.getQuestions().get(i);
            QuestionResultDto questionDto = new QuestionResultDto();
            questionDto.setId(questionFromDB.getId());
            questionDto.setTitle(questionFromDB.getText());
            result.getQuestions().add(questionDto);


            for (QuestionStandardAnswer questionStandardAnswer : questionFromDB.getQuestionStandardAnswers()) {

                skipStandardAnswerFlag = false;


                for (UserAnswer userAnswer : answers) {

                    //comparing standard answer with user answer by text and question they belong
                    if ( (questionStandardAnswer.getText().equals(userAnswer.getText()))
                            && (questionStandardAnswer.getTestQuestion().equals(userAnswer.getQuestion())) ) {
                        //collect only user chosen answer
                        questionDto.getAnswers().add(userAnswer);
                        skipStandardAnswerFlag = true;

                        if (userAnswer.getStatus().equals(AnswerStatus.CHOSEN_RIGHT)){
                            rightAnswerIter++;
                        }
                    }
                }

                //collect answers that were not chosen by user
                if (!skipStandardAnswerFlag) {
                    UserAnswer answer = new UserAnswer();
                    answer.setText(questionStandardAnswer.getText());
                    answer.setStatus(AnswerStatus.NOT_CHOSEN);
                    questionDto.getAnswers().add(answer);
                }
            }
            if (questionWithRightAnswerAmount.getRightAnswerAmount().equals(rightAnswerIter)){
                mark++;
                rightAnswerIter = 0;
            }
        }
        result.setMark(mark);
        return result;
    }

//    @Override
//    @Transactional
//    public CourseTest save(CourseTest courseTest) {
//
//        CourseTest courseTestFromDB = themeTestRepository.getById(courseTest.getId());
//
//
//        courseTestFromDB.setTestQuestions(courseTest.getTestQuestions());
//        themeTestRepository.save(courseTestFromDB);
//
//        for (TestQuestion question :
//                courseTest.getTestQuestions()) {
//            testQuestionService.save(question);
////            for (QuestionStandardAnswer answer: question.getQuestionStandardAnswers()){
////                questionStandardAnswerService.save(answer);
////            }
//        }
//
//        return courseTestFromDB;
//    }
}
