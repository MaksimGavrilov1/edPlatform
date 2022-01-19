package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.CourseThemeRepository;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.CourseThemeValidator;
import com.gavrilov.edPlatform.validator.CourseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final CourseValidator validator;
    private final CourseThemeValidator courseThemeValidator;
    private final CourseThemeRepository courseThemeRepository;
    private final AttemptService attemptService;

    @GetMapping("/all")
    public String showCourses(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("usersAmount", userService.findAll().size());
        model.addAttribute("user", user);
        model.addAttribute("courses", courseService.getAll());
        return "showCourses";
    }

    @GetMapping("/{id}")
    public String viewCourse(@PathVariable Long id, Model model, @AuthenticationPrincipal PlatformUser user) {
        Course course = courseService.findCourse(id);
        Boolean joinedFlag = user.getJoinedCourses().contains(course);
        List<Attempt> userAttempts = attemptService.findByUser(user);
        int userAttemptsAmount = 0;
        if (userAttempts != null){
            userAttemptsAmount = userAttempts.size();
        }
        if (course.getTest().getAmountOfAttempts() > userAttemptsAmount) {
            model.addAttribute("isAbleToPass", true);
        } else if (course.getTest().getAmountOfAttempts().equals(userAttemptsAmount)){
            model.addAttribute("isAbleToPass", false);
        }
        model.addAttribute("course", course);
        model.addAttribute("joinedFlag", joinedFlag);
        return "viewCourse";
    }

    @GetMapping("/owned/{id}")
    public String viewOwnedCourse(@PathVariable Long id, Model model) {
        Course course = courseService.findCourse(id);
        model.addAttribute("course", course);

        return "viewAuthorCourse";
    }

    @GetMapping("/usersCourses")
    public String showUserCourses(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("userCourses", courseService.findCoursesByAuthor(user));
        return "showUserCourses";
    }

    @GetMapping("/addCourse")
    public String addCourse(Model model) {
        model.addAttribute("course", new Course());
        return "addCourse";
    }

    @PostMapping("/addCourse")
    public String sendCourse(@ModelAttribute Course course,
                             BindingResult result,
                             @AuthenticationPrincipal PlatformUser user,
                             Model model) {

        course.setAuthor(user);
        validator.validate(course, result);

        if (result.hasErrors()) {
            return "addCourse";
        }

        Course c = courseService.createCourse(course, user.getId());

        return "redirect:/courses/usersCourses";
//        return String.format("redirect:/courses/addThemes/%d", c.getId());
    }

    @GetMapping("/newCourses")
    public String showCoursesToAccept(Model model) {
        model.addAttribute("courses", courseService.findCoursesAwaitingConfirmation());
        return "showNewCourses";
    }

    @GetMapping("/approve/{id}")
    public String approveCourse(@PathVariable Long id) {
        Course course = courseService.findCourse(id);
        course.setStatus(CourseStatus.APPROVED);
        courseService.save(course);
        PlatformUser user = course.getAuthor();
        user.setRole(Role.TEACHER);
        userService.saveUser(user);
        return "redirect:/courses/newCourses";
    }

    @PostMapping("/joinCourse")
    public String joinCourse(@ModelAttribute("course") Course course,
                             @AuthenticationPrincipal PlatformUser user,
                             Model model,
                             BindingResult result) {
        Course courseFromDB = courseService.findCourse(course.getId());
        user.getJoinedCourses().add(courseFromDB);
        model.addAttribute("course", courseFromDB);
        return String.format("redirect:/courses/%d", courseFromDB.getId());
    }

    @GetMapping("/testPage")
    public String showTestPage(Model model) {
        return "testPage";
    }
}
