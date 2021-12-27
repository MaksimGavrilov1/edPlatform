package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.FormTest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.model.TestQuestion;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.QuestionStandardAnswerService;
import com.gavrilov.edPlatform.service.TestQuestionService;
import com.gavrilov.edPlatform.service.ThemeTestService;
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
    private final QuestionStandardAnswerService standardAnswerService;
    private final TestQuestionService testQuestionService;

    @GetMapping("/constructor")
    public String testConstructor(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("courses", courseService.findCoursesByAuthor(user));
        model.addAttribute("formTest", new FormTest());
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
                standardAnswerService.save(answer);
            }
        }

        return "redirect:/courses/usersCourses";
    }

    @GetMapping("/edit/{courseId}")
    public String editTest(@PathVariable Long courseId,
                           Model model) {
        CourseTest test = courseService.findCourse(courseId).getTest();
        model.addAttribute("test", test);
        return "editTest";
    }

    @PostMapping("/saveTest")
    public String saveTest (Model model,
                            @ModelAttribute("test") CourseTest test,
                            BindingResult result){
        themeTestService.initSave(test);
        return "redirect:/courses/usersCourses";
    }
}
