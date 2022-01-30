package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.FormTest;
import com.gavrilov.edPlatform.dto.TestDto;
import com.gavrilov.edPlatform.dto.TestResultDto;
import com.gavrilov.edPlatform.exception.AccessTestException;
import com.gavrilov.edPlatform.exception.CourseExpiredException;
import com.gavrilov.edPlatform.exception.OutOfAttemptsException;
import com.gavrilov.edPlatform.model.*;
import com.gavrilov.edPlatform.model.enumerator.AnswerStatus;
import com.gavrilov.edPlatform.model.enumerator.CourseSubscriptionStatus;
import com.gavrilov.edPlatform.service.*;
import com.gavrilov.edPlatform.validator.CourseTestValidator;
import com.gavrilov.edPlatform.validator.FormTestValidator;
import com.gavrilov.edPlatform.validator.TestDtoValidator;
import com.gavrilov.edPlatform.validator.ThemeTestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final CourseService courseService;
    private final ThemeTestService themeTestService;
    private final ConversionService conversionService;
    private final QuestionStandardAnswerService questionStandardAnswerService;
    private final TestQuestionService testQuestionService;
    private final UserAnswerService userAnswerService;
    private final AttemptService attemptService;
    private final CourseTestValidator courseTestValidator;
    private final FormTestValidator formTestValidator;
    private final SubscriptionService subscriptionService;
    private final TestDtoValidator testDtoValidator;

    @GetMapping("/constructor")
    public String testConstructor(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("courses", courseService.findCoursesWithEmptyTestByAuthor(user));
        model.addAttribute("formTest", new FormTest());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "testConstructor";
    }

    @PostMapping("/addTest")
    public String addTest(Model model,
                          @ModelAttribute("formTest") FormTest formTest,
                          BindingResult bindingResult,
                          @AuthenticationPrincipal PlatformUser user) {

        formTestValidator.validate(formTest, bindingResult);
        CourseTest test = conversionService.convert(formTest, CourseTest.class);

        //themeTestValidator.validate(test, bindingResult);


        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findCoursesByAuthor(user));
            model.addAttribute("formTest", formTest);
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "testConstructor";
        }

        CourseTest testFromDB = themeTestService.initSave(test);

        //fill test with empty questions for next render
        for (int i = 0; i < formTest.getQuestionAmount(); i++) {
            TestQuestion quest = new TestQuestion();
            quest.setCourseTest(testFromDB);
            testQuestionService.save(quest);
        }

        //fill test question's with default answers for next render
        for (TestQuestion question :
                testQuestionService.findByTest(testFromDB)) {
            for (int i = 0; i < 6; i++) {
                QuestionStandardAnswer answer = new QuestionStandardAnswer();
                answer.setTestQuestion(question);
                questionStandardAnswerService.save(answer);
            }
        }

        return "redirect:/courses/usersCourses";
    }

    @GetMapping("/edit/{courseId}")
    public String editTest(@PathVariable Long courseId,
                           Model model,
                           @AuthenticationPrincipal PlatformUser user) {
        CourseTest test = courseService.findCourse(courseId).getTest();

        model.addAttribute("test", test);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "editTest";
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/saveTest")
    public String saveTest(Model model,
                           @ModelAttribute("test") CourseTest test,
                           BindingResult result) {
        courseTestValidator.validate(test, result);
        if (result.hasErrors()){
            model.addAttribute("test", test);
            return "editTest";
        }
        themeTestService.initSave(test);
        return "redirect:/courses/usersCourses";
    }

    @GetMapping("/render/{courseId}")
    public String renderTest(@PathVariable Long courseId,
                             Model model,
                             @AuthenticationPrincipal PlatformUser user) {

        Course courseFromDB = courseService.findCourse(courseId);

        if (!subscriptionService.isUserSubOnCourse(user, courseFromDB)){
            throw new AccessTestException("Вы не подписаны на курс");
        } else {
            subscriptionService.updateSubscriptionStatus(user);
        }
        if (!subscriptionService.findByUserAndCourse(user,courseFromDB).getStatus().equals(CourseSubscriptionStatus.OPEN)){
            throw new CourseExpiredException("Ваше время для выполнения теста закончилось");
        }

        CourseTest randAnswerTest = themeTestService.randomizeAnswers(courseFromDB.getTest());
        if (!attemptService.anyAttemptsLeft(user, randAnswerTest)){
            throw new OutOfAttemptsException("У вас закончились попытки");
        }
        attemptService.initAttempt(user, randAnswerTest);
        TestDto testDto = conversionService.convert(randAnswerTest, TestDto.class);

        model.addAttribute("test", testDto);
        model.addAttribute("courseName", courseFromDB.getName());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "passTest";
    }

    @PostMapping("/passTest")
    public String passTest(@ModelAttribute("test") TestDto test,
                           BindingResult result,
                           Model model,
                           @AuthenticationPrincipal PlatformUser user) {
        Course courseFromDB = courseService.findCourse(test.getCourseId());
        if (!subscriptionService.isUserSubOnCourse(user, courseFromDB)){
            throw new AccessTestException("Вы не подписаны на курс");
        } else {
            subscriptionService.updateSubscriptionStatus(user);
        }
        if (!subscriptionService.findByUserAndCourse(user,courseFromDB).getStatus().equals(CourseSubscriptionStatus.OPEN)){
            throw new CourseExpiredException("Ваше время для выполнения теста закончилось");
        }
        testDtoValidator.validate(test, result);
        if (result.hasErrors()){
            model.addAttribute("test", test);
            model.addAttribute("courseName", courseFromDB.getName());
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "passTest";
        }
        TestResultDto testResult = themeTestService.calculateResult(test, user);
        model.addAttribute("result", testResult);
        model.addAttribute("chosenRight", AnswerStatus.CHOSEN_RIGHT);
        model.addAttribute("chosenWrong", AnswerStatus.CHOSEN_WRONG);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "testResult";
    }

    @GetMapping("/showResult/{attemptId}")
    public String showTestResult(@PathVariable("attemptId") Long attemptId,
                                 Model model,
                                 @AuthenticationPrincipal PlatformUser user) {

        Attempt attempt = attemptService.findById(attemptId);
        List<UserAnswer> answers = userAnswerService.findByAttempt(attempt);
        model.addAttribute("result", themeTestService.formResult(answers, attemptId));
        model.addAttribute("chosenRight", AnswerStatus.CHOSEN_RIGHT);
        model.addAttribute("chosenWrong", AnswerStatus.CHOSEN_WRONG);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "testResult";
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("/moderator/view/{id}")
    public String viewCourseToApprove(Model model, @AuthenticationPrincipal PlatformUser user, @PathVariable Long id){
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("test", themeTestService.findTest(id));
        return "viewTestForModer";
    }
}
