package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.FormCourse;
import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Subscription;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import com.gavrilov.edPlatform.model.enumerator.CourseSubscriptionStatus;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.CourseThemeRepository;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.SubscriptionService;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.CourseEditValidator;
import com.gavrilov.edPlatform.validator.CourseThemeValidator;
import com.gavrilov.edPlatform.validator.FormCourseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final FormCourseValidator validator;
    private final CourseThemeValidator courseThemeValidator;
    private final CourseThemeRepository courseThemeRepository;
    private final AttemptService attemptService;
    private final CourseEditValidator courseEditValidator;
    private final ConversionService conversionService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/all")
    public String showCourses(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("popularCourses", courseService.findTenMostPopular());
        model.addAttribute("newCourses", courseService.findTenNewest());
        model.addAttribute("usersAmount", userService.findAll().size());
        model.addAttribute("user", user);
        if (user != null) {
            model.addAttribute("userProfileName", user.getProfile().getName());
        }
        model.addAttribute("courses", courseService.getAll());
        return "showCourses";
    }

    @GetMapping("/{id}")
    public String viewCourse(@PathVariable Long id, Model model, @AuthenticationPrincipal PlatformUser user) {
        subscriptionService.updateSubscriptionStatus(user);
        Course course = courseService.findCourse(id);
        Boolean joinedFlag = false;
        if (subscriptionService.isUserSubOnCourse(user, course)) {
            joinedFlag = true;
        }
        List<Attempt> userAttempts = attemptService.findByUserAndTest(user, course.getTest());
        int userAttemptsLeft = course.getTest().getAmountOfAttempts();
        int maxMark = course.getTest().getTestQuestions().size();
        if (userAttempts != null) {
            userAttemptsLeft = userAttemptsLeft - userAttempts.size();
        } else {
            userAttempts = new ArrayList<>();
        }
        model.addAttribute("sub", subscriptionService.findByUserAndCourse(user, course));
        model.addAttribute("maxMark", maxMark);
        model.addAttribute("attempts", userAttempts);
        model.addAttribute("attemptsLeft", userAttemptsLeft);
        model.addAttribute("course", course);
        model.addAttribute("joinedFlag", joinedFlag);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "viewCourse";
    }

    @GetMapping("/owned/{id}")
    public String viewOwnedCourse(@PathVariable Long id, Model model, @AuthenticationPrincipal PlatformUser user) {
        Course course = courseService.findCourse(id);
        if (!course.getAuthor().equals(user)) {
            return "redirect:/courses/all";
        }
        model.addAttribute("course", course);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "viewAuthorCourse";
    }

    @GetMapping("/usersCourses")
    public String showUserCourses(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("userCourses", courseService.findCoursesByAuthor(user));
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "showUserCourses";
    }

    @GetMapping("/addCourse")
    public String addCourse(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("course", new FormCourse());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "addCourse";
    }

    @PostMapping("/addCourse")
    public String sendCourse(@ModelAttribute("course") FormCourse course,
                             BindingResult result,
                             @AuthenticationPrincipal PlatformUser user,
                             Model model) {

        course.setAuthor(user);
        validator.validate(course, result);

        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "addCourse";
        }
        Course c = conversionService.convert(course, Course.class);
        courseService.save(c);

        return "redirect:/courses/usersCourses";
//        return String.format("redirect:/courses/addThemes/%d", c.getId());
    }

    @GetMapping("/newCourses")
    public String showCoursesToAccept(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("courses", courseService.findCoursesAwaitingConfirmation());
        model.addAttribute("userProfileName", user.getProfile().getName());
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

    @GetMapping("/joinCourse/{courseId}")
    public String joinCourse(@PathVariable Long courseId,
                             @AuthenticationPrincipal PlatformUser user,
                             Model model) {
        Course courseFromDB = courseService.findCourse(courseId);
        PlatformUser userFromDB = userService.findByUsername(user.getUsername());
        if (subscriptionService.isUserSubOnCourse(user, courseFromDB)) {
            return String.format("redirect:/courses/%d", courseFromDB.getId());
        }


        userFromDB.getJoinedCourses().add(courseFromDB);
        courseFromDB.getJoinedUsers().add(userFromDB);
        userService.saveUser(userFromDB);
        Course newCourse = courseService.save(courseFromDB);

        Subscription sub = new Subscription();
        sub.setCourse(courseFromDB);
        sub.setUser(userFromDB);
        sub.setDateOfSubscription(new Timestamp(new Date().getTime()));
        long endOfSub = sub.getDateOfSubscription().getTime() + courseFromDB.getActiveTime().getTime();
        sub.setCourseEndDate(new Timestamp(endOfSub));
        sub.setStatus(CourseSubscriptionStatus.OPEN);
        subscriptionService.save(sub);
        //user.getJoinedCourses().add(courseFromDB);
        model.addAttribute("course", courseFromDB);
        return String.format("redirect:/courses/%d", courseFromDB.getId());
    }

    @GetMapping("/edit/{courseId}")
    public String renderEditCourse(Model model,
                                   @PathVariable Long courseId,
                                   @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("userProfileName", user.getProfile().getName());
        Course course = courseService.findCourse(courseId);
        FormCourse formCourse = conversionService.convert(course, FormCourse.class);
        model.addAttribute("course", formCourse);
        return "editCourse";
    }

    @PostMapping("/edit")
    public String editCourse(Model model,
                             @ModelAttribute("course") FormCourse course,
                             @AuthenticationPrincipal PlatformUser user,
                             BindingResult result) {
        course.setAuthor(user);
        validator.validate(course, result);
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "editCourse";
        }

        Course courseFromDB = courseService.findCourse(course.getId());
        courseFromDB.setName(course.getName());
        courseFromDB.setDescription(course.getDescription());
        courseService.save(courseFromDB);
        return String.format("redirect:/courses/owned/%d", course.getId());
    }

    @GetMapping("/search")
    public String renderCourseSearch(Model model,
                                     @AuthenticationPrincipal PlatformUser user,
                                     @RequestParam(value = "name", required = false) String name) {

        model.addAttribute("userProfileName", user.getProfile().getName());
        if (name != null){
            model.addAttribute("courses", courseService.findByPartName(name));
        } else {
            model.addAttribute("courses", courseService.getAll());
        }
        return "courseSearch";
    }
}
