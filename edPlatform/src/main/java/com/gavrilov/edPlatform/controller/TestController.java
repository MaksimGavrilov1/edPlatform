package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.FormTest;
import com.gavrilov.edPlatform.dto.TestDto;
import com.gavrilov.edPlatform.dto.TestResultDto;
import com.gavrilov.edPlatform.model.*;
import com.gavrilov.edPlatform.model.enumerator.AnswerStatus;
import com.gavrilov.edPlatform.service.*;
import com.gavrilov.edPlatform.validator.CourseTestValidator;
import com.gavrilov.edPlatform.validator.ThemeTestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final CourseService courseService;
    private final ThemeTestService themeTestService;
    private final ConversionService conversionService;
    private final ThemeTestValidator themeTestValidator;
    private final QuestionStandardAnswerService questionStandardAnswerService;
    private final TestQuestionService testQuestionService;
    private final UserAnswerService userAnswerService;
    private final AttemptService attemptService;
    private final CourseTestValidator courseTestValidator;

    @GetMapping("/constructor")
    public String testConstructor(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("courses", courseService.findCoursesByAuthor(user));
        model.addAttribute("formTest", new FormTest());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "testConstructor";
    }

    @PostMapping("/addTest")
    public String addTest(Model model,
                          @ModelAttribute("formTest") FormTest formTest,
                          BindingResult bindingResult,
                          @AuthenticationPrincipal PlatformUser user) {

        CourseTest test = conversionService.convert(formTest, CourseTest.class);

        themeTestValidator.validate(test, bindingResult);


        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findCoursesByAuthor(user));
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

    @PostMapping("/render")
    public String renderTest(@ModelAttribute("course") Course course,
                             BindingResult result,
                             Model model,
                             @AuthenticationPrincipal PlatformUser user) {
        Course courseFromDB = courseService.findCourse(course.getId());
        CourseTest randAnswerTest = themeTestService.randomizeAnswers(courseFromDB.getTest());
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
        //TestResultDto testResult = conversionService.convert(test, TestResultDto.class);
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
}
