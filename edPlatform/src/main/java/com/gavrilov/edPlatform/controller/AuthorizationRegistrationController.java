package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.model.*;
import com.gavrilov.edPlatform.model.enumerator.CourseSubscriptionStatus;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.PlatformUserProfileRepository;
import com.gavrilov.edPlatform.service.*;
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

import java.sql.Timestamp;
import java.util.Date;
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
    private final CourseThemeService courseThemeService;
    private final ThemeTestService testService;
    private final TestQuestionService testQuestionService;
    private final QuestionStandardAnswerService questionStandardAnswerService;
    private final SubscriptionService subscriptionService;
    private final TagService tagService;
    boolean flag = false;

    @GetMapping("/login")
    public String login(Model model, String error, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("error", error);
        if (!flag) {
            PlatformUser admin = new PlatformUser();
            PlatformUserProfile profile = new PlatformUserProfile();
            admin.setRole(Role.ADMIN);
            admin.setUsername("superadmin");
            admin.setPassword(encoder.encode("superadmin"));
            admin.setProfile(profile);
            PlatformUser newAdm = userService.saveUser(admin);
            profile.setName("Ivan");
            profile.setSurname("Ivanov");
            profile.setMiddleName("Ivanovich");
            profile.setSelfDescription("I m admin");
            profile.setPlatformUser(newAdm);
            platformUserProfileRepository.save(profile);

            PlatformUser moder1 = new PlatformUser();
            PlatformUserProfile mprofile = new PlatformUserProfile();
            moder1.setRole(Role.MODERATOR);
            moder1.setUsername("moderator123");
            moder1.setPassword(encoder.encode("qwerty12"));
            moder1.setProfile(mprofile);
            PlatformUser newMOd = userService.saveUser(moder1 );
            mprofile.setName("Николай");
            mprofile.setSurname("Иванов");
            mprofile.setMiddleName("Николаевич");
            mprofile.setSelfDescription("Я преподаю информатику с 2008 года, когда предмет ещё назывался ИКТ. Начинал со школы, учил детей разбираться\n" +
                    "          в программировании и сдавать ЕГЭ на 90 баллов и выше. За два года вывел нашу школу на второе место в районе по\n" +
                    "          олимпиадам по информатике. Вёл два класса коррекции — пятый и одиннадцатый — и знаю, как объяснить основы\n" +
                    "          теории вероятности даже тем, кто не хочет ничему учиться.");
            mprofile.setPlatformUser(newMOd);
            platformUserProfileRepository.save(mprofile);

            PlatformUser student = new PlatformUser();
            PlatformUserProfile studProf = new PlatformUserProfile();
            student.setRole(Role.STUDENT);
            student.setUsername("qwerty12");
            student.setPassword(encoder.encode("qwerty12"));
            PlatformUser newStudent = userService.saveUser(student);
            studProf.setName("Николай");
            studProf.setSurname("Николаев");
            studProf.setMiddleName("Николаевич");
            studProf.setSelfDescription("Я преподаю информатику с 2008 года, когда предмет ещё назывался ИКТ. Начинал со школы, учил детей разбираться\n" +
                    "          в программировании и сдавать ЕГЭ на 90 баллов и выше. За два года вывел нашу школу на второе место в районе по\n" +
                    "          олимпиадам по информатике. Вёл два класса коррекции — пятый и одиннадцатый — и знаю, как объяснить основы\n" +
                    "          теории вероятности даже тем, кто не хочет ничему учиться.");
            studProf.setPlatformUser(newStudent);
            platformUserProfileRepository.save(studProf);
            //PlatformUser newStudent = userService.saveUser(student);

            PlatformUser tempUser = new PlatformUser();
            tempUser.setRole(Role.STUDENT);
            tempUser.setUsername("qwerty123");
            tempUser.setPassword(encoder.encode("qwerty12"));
            PlatformUser tempUser1 = userService.saveUser(tempUser);


            Course course = new Course();
            course.setAuthor(newStudent);
            course.setName("Введение в программирование. Курс для начинающих.");
            course.setDescription("Этот курс предназначен для тех, кто хочет стать программистом. Избранные разделы высшей математики в контексте Data Science с упором на решение задач. Для сильных духом.");
            course.setActiveTime(new Timestamp(7_000));
            course.setIsAlwaysOpen(true);
            Course newCourse = courseService.save(course);
//            Subscription sub1 = new Subscription();
//            sub1.setCourse(newCourse);
//            sub1.setUser(newStudent);
//            sub1.setStatus(CourseSubscriptionStatus.OPEN);
//            sub1.setDateOfSubscription(new Timestamp(new Date().getTime()));
//            sub1.setCourseEndDate(new Timestamp(new Date().getTime() + 5000));
//            subscriptionService.save(sub1);


            Course course1 = new Course();
            course1.setAuthor(newStudent);
            course1.setName("Структуры данных предметной области");
            course1.setDescription("Целью курса «Структуры данных в предметной области» является изучение структур данных, которые используются при программировании решения задач из предметной области указанной специальности и освоение алгоритмов обработки данных (на примерах из предметной области деятельности).");
            courseService.save(course1);
//            Subscription sub2 = new Subscription();
//            sub2.setCourse(course1);
//            sub2.setUser(tempUser1);
//            Subscription sub3 = new Subscription();
//            sub3.setCourse(course1);
//            sub3.setUser(admin);
//            subscriptionService.save(sub2);
//            subscriptionService.save(sub3);

            //tags for 2 courses
            Tag tag = new Tag();
            tag.setName("программирование");
            tag.getCourses().add(course);
            tag.getCourses().add(course1);
            tagService.save(tag);
            Tag tag1 = new Tag();
            tag1.setName("алгоритмы");
            tag1.getCourses().add(course);
            tag1.getCourses().add(course1);
            tagService.save(tag1);
            Tag tag2 = new Tag();
            tag2.setName("java");
            tag2.getCourses().add(course);
            tagService.save(tag2);

            Tag tagger = new Tag();
            for(int i=0; i<100; i ++){
                tagger.setName(" " + i);
                tagService.save(tagger);
            }


            Course course2 = new Course();
            course2.setAuthor(newStudent);
            course2.setName("Функциональное программирование на F#");
            course2.setDescription("Излагаются принципы написания программ в рамках функциональной парадигмы программирования. В качестве основного языка взят F#, интегрированный в среду разработки Visual Studio 2019, но приводятся соответствующие примеры на других языках.");
            courseService.save(course2);

            Course course3 = new Course();
            course3.setAuthor(newStudent);
            course3.setName("Легкий старт в Java. Вводный курс для чайников");
            course3.setDescription("Вводный курс по языку программирования Java. Доступно изложенный материал и большое количество задач.");
            courseService.save(course3);
//            Subscription sub4 = new Subscription();
//            sub4.setCourse(course3);
//            sub4.setUser(newStudent);
//            Subscription sub5 = new Subscription();
//            sub5.setCourse(course3);
//            sub5.setUser(tempUser);
//            subscriptionService.save(sub4);
//            subscriptionService.save(sub5);

            CourseTheme theme1 = new CourseTheme();
            theme1.setName("Алгоритмы");
            theme1.setLectureMaterial("Конечная совокупность точно заданных правил решения некоторого класса задач или набор инструкций, описывающих порядок действий исполнителя для решения определённой задачи. В старой трактовке вместо слова «порядок» использовалось слово «последовательность», но по мере развития параллельности в работе компьютеров слово «последовательность» стали заменять более общим словом «порядок». Независимые инструкции могут выполняться в произвольном порядке, параллельно, если это позволяют используемые исполнители.");
            theme1.setCourse(newCourse);
            courseThemeService.saveTheme(theme1);
            CourseTheme theme2 = new CourseTheme();
            theme2.setName("Типы данных");
            theme2.setLectureMaterial("Одной из основных особенностей Java является то, что данный язык является строго типизированным. А это значит, что каждая переменная и константа представляет определенный тип и данный тип строго определен. Тип данных определяет диапазон значений, которые может хранить переменная или константа.\n" +
                    "\n" +
                    "Итак, рассмотрим систему встроенных базовых типов данных, которая используется для создания переменных в Java. А она представлена следующими типами.");
            theme2.setCourse(newCourse);
            courseThemeService.saveTheme(theme2);
            CourseTheme theme3 = new CourseTheme();
            theme3.setName("Языки программирования");
            theme3.setLectureMaterial("Язы́к программи́рования — формальный язык, предназначенный для записи компьютерных программ.");
            theme3.setCourse(newCourse);
            courseThemeService.saveTheme(theme3);

            CourseTest test = new CourseTest();
            test.setName("Тест для проверки знаний");
            test.setCourse(newCourse);
            test.setAmountOfAttempts(7);
            test.setMinThreshold(1);
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
    public String register(@ModelAttribute(value = "userDto") UserDto userDto, BindingResult result) {
        userDtoValidator.validate(userDto, result);
        if (result.hasErrors()) {
            return "register";
        }
        PlatformUser user = conversionService.convert(userDto, PlatformUser.class);
        String userPassword = Objects.requireNonNull(user).getPassword();
        user.setPassword(encoder.encode(userPassword));
        PlatformUser newUser = userService.saveUser(user);
        if (newUser != null){
            return "redirect:/successfulRegistration";
        }
        return "successfulRegister";
    }

    @GetMapping("/successfulRegistration")
    public String renderSuccessfulRegistration(){
        return "successfulRegister";
    }
}
