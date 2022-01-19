package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.model.*;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.PlatformUserProfileRepository;
import com.gavrilov.edPlatform.service.*;
import com.gavrilov.edPlatform.service.themeTestServiceImpl.ThemeTestServiceImpl;
import com.gavrilov.edPlatform.validator.PlatformUserValidator;
import com.gavrilov.edPlatform.validator.UserDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.management.QueryEval;
import java.util.Objects;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthorizationRegistrationController {

    private final UserService userService;
    private final PlatformUserValidator userValidator;
    private final UserDtoValidator userDtoValidator;
    private final BCryptPasswordEncoder encoder;
    private final ConversionService conversionService;
    private final PlatformUserProfileRepository platformUserProfileRepository;
    private final CourseService courseService;
    private final ThemeTestService testService;
    private final TestQuestionService testQuestionService;
    private final QuestionStandardAnswerService questionStandardAnswerService;
    boolean flag = false;

    @GetMapping("/login")
    public String login(Model model, String error, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("error", error);
        if (!flag){
            PlatformUser admin = new PlatformUser();
            PlatformUserProfile profile = new PlatformUserProfile();
            admin.setRole(Role.ADMIN);
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("qwerty"));
            admin.setProfile(profile);
            profile.setName("Ivan");
            profile.setSurname("Ivanov");
            profile.setMiddleName("Ivanovich");
            profile.setSelfDescription("I m admin");
            profile.setPlatformUser(admin);
            userService.saveUser(admin);

            PlatformUser student = new PlatformUser();
            PlatformUserProfile studProf = new PlatformUserProfile();
            student.setRole(Role.STUDENT);
            student.setUsername("qwerty12");
            student.setPassword(encoder.encode("qwerty"));
            PlatformUser newStudent = userService.saveUser(student);
            studProf.setName("Николай");
            studProf.setSurname("Николаев");
            studProf.setMiddleName("Николаевич");
            studProf.setSelfDescription("I m student");
            studProf.setPlatformUser(newStudent);
            platformUserProfileRepository.save(studProf);
            //PlatformUser newStudent = userService.saveUser(student);


            Course course = new Course();
            course.setAuthor(newStudent);
            course.setName("Введение в программирование. Курс для начинающих.");
            course.setDescription("Этот курс предназначен для тех, кто хочет стать программистом");
            Course newCourse = courseService.save(course);

            CourseTest test = new CourseTest();
            test.setName("Тест для проверки знаний");
            test.setCourse(newCourse);
            test.setAmountOfAttempts(1);
            CourseTest newTest = testService.initSave(test);

            TestQuestion quest1 = new TestQuestion();
            quest1.setCourseTest(newTest);
            quest1.setText("Выберите правильный вариант записи операции сравнения?");
            TestQuestion newQuest1 = testQuestionService.save(quest1);

            QuestionStandardAnswer ans1 = new QuestionStandardAnswer();
            ans1.setText("x>=0;");
            ans1.setRight(true);
            ans1.setTestQuestion(newQuest1);
            questionStandardAnswerService.save(ans1);
            QuestionStandardAnswer ans2 = new QuestionStandardAnswer();
            ans2.setText("0<=x;");
            ans2.setRight(true);
            ans2.setTestQuestion(newQuest1);
            questionStandardAnswerService.save(ans2);
            QuestionStandardAnswer ans3 = new QuestionStandardAnswer();
            ans3.setText("x=>0;");
            ans3.setRight(false);
            ans3.setTestQuestion(newQuest1);
            questionStandardAnswerService.save(ans3);
            QuestionStandardAnswer ans4 = new QuestionStandardAnswer();
            ans4.setText("0=<x;");
            ans4.setRight(false);
            ans4.setTestQuestion(newQuest1);
            questionStandardAnswerService.save(ans4);
            QuestionStandardAnswer ans5 = new QuestionStandardAnswer();
            ans5.setText("x=0;");
            ans5.setRight(false);
            ans5.setTestQuestion(newQuest1);
            questionStandardAnswerService.save(ans5);
            QuestionStandardAnswer ans6 = new QuestionStandardAnswer();
            ans6.setText("0=x;");
            ans6.setRight(false);
            ans6.setTestQuestion(newQuest1);
            questionStandardAnswerService.save(ans6);

            TestQuestion quest2 = new TestQuestion();
            quest2.setCourseTest(test);
            quest2.setText("Какие диаграммы служат основой для генерации кода на целевой языке программирования?");
            TestQuestion newQuest2 = testQuestionService.save(quest2);

            QuestionStandardAnswer ans1_2 = new QuestionStandardAnswer();
            ans1_2.setText("Диаграммы классов");
            ans1_2.setRight(true);
            ans1_2.setTestQuestion(newQuest2);
            questionStandardAnswerService.save(ans1_2);
            QuestionStandardAnswer ans2_2 = new QuestionStandardAnswer();
            ans2_2.setText("Диаграммы Вариантов использования");
            ans2_2.setRight(false);
            ans2_2.setTestQuestion(newQuest2);
            questionStandardAnswerService.save(ans2_2);
            QuestionStandardAnswer ans3_2 = new QuestionStandardAnswer();
            ans3_2.setText("Диаграммы последовательности");
            ans3_2.setRight(false);
            ans3_2.setTestQuestion(newQuest2);
            questionStandardAnswerService.save(ans3_2);
            QuestionStandardAnswer ans4_2 = new QuestionStandardAnswer();
            ans4_2.setText("Диаграммы состояний");
            ans4_2.setRight(false);
            ans4_2.setTestQuestion(newQuest2);
            questionStandardAnswerService.save(ans4_2);
            QuestionStandardAnswer ans5_2 = new QuestionStandardAnswer();
            ans5_2.setText("Диаграммы крутые");
            ans5_2.setRight(false);
            ans5_2.setTestQuestion(newQuest2);
            questionStandardAnswerService.save(ans5_2);
            QuestionStandardAnswer ans6_2 = new QuestionStandardAnswer();
            ans6_2.setText("Диаграммы не крутые");
            ans6_2.setRight(false);
            ans6_2.setTestQuestion(newQuest2);
            questionStandardAnswerService.save(ans6_2);

            flag = true;
        }

        if (user != null) {
            return "redirect:/courses/all";
        }
        return "login";
    }


    @GetMapping("/register")
    public String registration(Model model, @AuthenticationPrincipal PlatformUser user) {

        if (user != null) {
            return "redirect:/courses/all";
        }

        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(value = "userDto")UserDto userDto, BindingResult result){
        userDtoValidator.validate(userDto, result);
        if (result.hasErrors()){
            return "register";
        }
        PlatformUser user = conversionService.convert(userDto, PlatformUser.class);
        String userPassword = Objects.requireNonNull(user).getPassword();
        user.setPassword(encoder.encode(userPassword));
        PlatformUser newUser = userService.saveUser(user);
        return "successfulRegister";
    }
}
