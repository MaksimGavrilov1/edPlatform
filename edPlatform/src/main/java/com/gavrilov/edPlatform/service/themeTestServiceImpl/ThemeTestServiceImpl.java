package com.gavrilov.edPlatform.service.themeTestServiceImpl;

import com.gavrilov.edPlatform.dto.AnswerDto;
import com.gavrilov.edPlatform.dto.FormTest;
import com.gavrilov.edPlatform.dto.QuestionDto;
import com.gavrilov.edPlatform.dto.QuestionResultDto;
import com.gavrilov.edPlatform.dto.TestDto;
import com.gavrilov.edPlatform.dto.TestResultDto;
import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.model.TestQuestion;
import com.gavrilov.edPlatform.model.UserAnswer;
import com.gavrilov.edPlatform.model.enumerator.AnswerStatus;
import com.gavrilov.edPlatform.repo.ThemeTestRepository;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.QuestionStandardAnswerService;
import com.gavrilov.edPlatform.service.TestQuestionService;
import com.gavrilov.edPlatform.service.ThemeTestService;
import com.gavrilov.edPlatform.service.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
    private final AttemptService attemptService;

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
        for (TestQuestion question : test.getTestQuestions()) {
            List<QuestionStandardAnswer> randomAnswers = this.<QuestionStandardAnswer>randomizeAnswerOrder(question.getQuestionStandardAnswers());
            question.setQuestionStandardAnswers(randomAnswers);
        }
        return test;
    }

    private <T> List<T> randomizeAnswerOrder(List<T> answers) {

        List<T> result = new ArrayList<>();
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
        testFromDB = randomizeAnswers(testFromDB);

        //form a result object that will render at view
        TestResultDto result = new TestResultDto();
        result.setName(source.getName());

        //create attempt
        Attempt attempt = attemptService.findLastAttemptByUserAndTest(user, testFromDB);
        attempt.setCourseTest(testFromDB);
        attempt.setUser(user);
        attempt.setTime(new Timestamp(new Date().getTime()));
        Attempt attemptFromDB = attemptService.save(attempt);

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
            Integer wrongAnswerIter = 0;

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
                            answerResult.setAttempt(attemptFromDB);
                            userAnswerService.save(answerResult);
                            rightAnswerIter++;
                        }
                        if (!answerFromDB.getRight() && answerFromSource.getChecked()) {
                            answerResult.setStatus(AnswerStatus.CHOSEN_WRONG);
                            answerResult.setUser(user);
                            answerResult.setAttempt(attemptFromDB);
                            userAnswerService.save(answerResult);
                            wrongAnswerIter++;
                        }
                    }
                }
            }

            if (rightAnswerIter.equals(questionFromSource.getRightAnswerAmount()) && wrongAnswerIter.equals(0)) {
                mark++;
                questionResult.setAddsAPoint(true);
            }

        }
        attemptFromDB.setPass(mark >= testFromDB.getMinThreshold());
        result.setMark(mark);
        attemptFromDB.setMark(mark);
        attemptService.save(attemptFromDB);
        return result;
    }

    public TestResultDto formResult(List<UserAnswer> answers, Long attemptId) {
        Attempt attempt = attemptService.findById(attemptId);
        CourseTest test = attempt.getCourseTest();
        test = randomizeAnswers(test);
        //variable with right answer amount
        TestDto testDto = conversionService.convert(test, TestDto.class);
        TestResultDto result = new TestResultDto();
        result.setName(test.getName());
        result.setMark(attempt.getMark());
        int rightAnswerIter;
        int wrongAnswerIter;
        boolean skipStandardAnswerFlag;
        for (int i = 0; i < test.getTestQuestions().size(); i++) {
            TestQuestion questionFromDB = test.getTestQuestions().get(i);
            QuestionDto questionWithRightAnswerAmount = testDto.getQuestions().get(i);
            QuestionResultDto questionDto = new QuestionResultDto();
            questionDto.setId(questionFromDB.getId());
            questionDto.setTitle(questionFromDB.getText());
            result.getQuestions().add(questionDto);
            rightAnswerIter = 0;
            wrongAnswerIter = 0;
            for (QuestionStandardAnswer questionStandardAnswer : questionFromDB.getQuestionStandardAnswers()) {
                skipStandardAnswerFlag = false;
                for (UserAnswer userAnswer : answers) {
                    //comparing standard answer with user answer by text and question they belong
                    if ((questionStandardAnswer.getText().equals(userAnswer.getText()))
                            && (questionStandardAnswer.getTestQuestion().equals(userAnswer.getQuestion()))) {
                        //collect only user chosen answer
                        questionDto.getAnswers().add(userAnswer);
                        skipStandardAnswerFlag = true;
                        if (userAnswer.getStatus().equals(AnswerStatus.CHOSEN_RIGHT)) {
                            rightAnswerIter++;
                        } else if (userAnswer.getStatus().equals(AnswerStatus.CHOSEN_WRONG)) {
                            wrongAnswerIter++;
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
            if (questionWithRightAnswerAmount.getRightAnswerAmount().equals(rightAnswerIter) && wrongAnswerIter == 0) {
                rightAnswerIter = 0;
                questionDto.setAddsAPoint(true);
            }
            wrongAnswerIter = 0;
        }
        return result;
    }

    @Override
    public void initTestContent(FormTest formTest, CourseTest test) {
        //fill test with empty questions for next render
        for (int i = 0; i < formTest.getQuestionAmount(); i++) {
            TestQuestion quest = new TestQuestion();
            quest.setCourseTest(test);
            testQuestionService.save(quest);
        }

        //fill test question's with default answers for next render
        for (TestQuestion question :
                testQuestionService.findByTest(test)) {
            for (int i = 0; i < 6; i++) {
                QuestionStandardAnswer answer = new QuestionStandardAnswer();
                answer.setTestQuestion(question);
                questionStandardAnswerService.save(answer);
            }
        }
    }

}
