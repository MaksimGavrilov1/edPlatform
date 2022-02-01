package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.FormCourse;
import com.gavrilov.edPlatform.dto.TagDto;
import com.gavrilov.edPlatform.exception.CourseAccessException;
import com.gavrilov.edPlatform.exception.CourseEditingException;
import com.gavrilov.edPlatform.exception.CourseSubmitException;
import com.gavrilov.edPlatform.exception.CourseSubscribeException;
import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import com.gavrilov.edPlatform.service.*;
import com.gavrilov.edPlatform.validator.FormCourseEditValidator;
import com.gavrilov.edPlatform.validator.FormCourseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.gavrilov.edPlatform.constant.PlatformValidationUtilities.maxTagAmount;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final FormCourseValidator validator;
    private final FormCourseEditValidator editValidator;
    private final AttemptService attemptService;
    private final ConversionService conversionService;
    private final SubscriptionService subscriptionService;
    private final TagService tagService;
    private final CourseConfirmationRequestService courseConfirmationRequestService;
    private final MapSessionRepository sessionRepository;



    @GetMapping("/all")
    public String showCourses(Model model, @AuthenticationPrincipal PlatformUser user) {

        model.addAttribute("newCourses", courseService.findTenNewest(user));
        model.addAttribute("usersAmount", userService.findAll().size());
        model.addAttribute("user", user);
        if (user != null) {
            model.addAttribute("userProfileName", user.getProfile().getName());
        }
        model.addAttribute("popularCourses", courseService.findTenMostPopular(user));
        return "course/showCourses";
    }

    @GetMapping("/view/{id}")
    public String viewCourse(@PathVariable Long id, Model model, @AuthenticationPrincipal PlatformUser user) {
        Course course = courseService.findCourse(id);
        if (course.getStatus().equals(CourseStatus.DRAFT) || course.getStatus().equals(CourseStatus.AWAITING_CONFIRMATION)){
            throw new CourseAccessException("Вы не можете просматривать этот курс, так как он еще не одобрен");
        }
        if (course.getStatus().equals(CourseStatus.ARCHIVED)){
            throw new CourseAccessException("Курс архивирован");
        }
        if (user != null) {
            boolean joinedFlag = subscriptionService.isUserSubOnCourse(user, course);
            if (joinedFlag) {
                subscriptionService.updateSubscriptionStatusByUserAndCourse(user, course);
            }
            if (user.equals(course.getAuthor())){
                model.addAttribute("authorFlag", true);
            } else {
                model.addAttribute("authorFlag", false);
            }
            List<Attempt> userAttempts = attemptService.findByUserAndTest(user, course.getTest());
            int userAttemptsLeft = course.getTest().getAmountOfAttempts() - userAttempts.size();
            int maxMark = course.getTest().getTestQuestions().size();
            model.addAttribute("sub", subscriptionService.findByUserAndCourse(user, course));
            model.addAttribute("maxMark", maxMark);
            model.addAttribute("attempts", userAttempts);
            model.addAttribute("attemptsLeft", userAttemptsLeft);
            model.addAttribute("joinedFlag", joinedFlag);
            model.addAttribute("userProfileName", user.getProfile().getName());
        } else {
            model.addAttribute("joinedFlag", false);
            model.addAttribute("authorFlag", false);
        }
        model.addAttribute("course", course);

        return "course/viewCourse";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/owned/{id}")
    public String viewOwnedCourse(@PathVariable Long id, Model model, @AuthenticationPrincipal PlatformUser user) {
        Course course = courseService.findCourse(id);
        if (!course.getAuthor().equals(user)) {
            throw new CourseEditingException("Вы не являетесь автором этого курса");
        }
        model.addAttribute("course", course);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "course/viewAuthorCourse";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/usersCourses")
    public String showUserCourses(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("userCourses", courseService.findCoursesByAuthor(user));
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("requests", courseConfirmationRequestService.findByUser(user));
        model.addAttribute("declined", RequestStatus.DECLINED);
        model.addAttribute("draft", CourseStatus.DRAFT);
        ExpiringSession session = sessionRepository.getSession(user.getUsername());
        return "course/showUserCourses";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/addCourse")
    public String addCourse(Model model, @AuthenticationPrincipal PlatformUser user) {
        FormCourse formCourse = new FormCourse();
        for (int i = 0; i < 3; i++) {
            formCourse.getTags().add(new TagDto());
        }
        model.addAttribute("course", formCourse);
        model.addAttribute("tagCollection", tagService.findAll());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "course/addCourse";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @PostMapping("/addCourse")
    public String sendCourse(@ModelAttribute("course") FormCourse course,
                             BindingResult result,
                             @AuthenticationPrincipal PlatformUser user,
                             Model model) {

        course.setAuthor(user);
        validator.validate(course, result);
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("tagCollection", tagService.findAll());
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "course/addCourse";
        }
        Course c = conversionService.convert(course, Course.class);
        Course newCourse = courseService.save(c);
        return "redirect:/courses/usersCourses";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/joinCourse/{courseId}")
    public String joinCourse(@PathVariable Long courseId,
                             @AuthenticationPrincipal PlatformUser user,
                             Model model) {
        Course courseFromDB = courseService.findCourse(courseId);
        PlatformUser userFromDB = userService.findByUsername(user.getUsername());
        if (subscriptionService.isUserSubOnCourse(user, courseFromDB)) {
            throw new CourseSubscribeException("Вы уже подписаны на этот курс");
        }
        if (user.equals(courseFromDB.getAuthor())){
            throw new CourseSubscribeException("Вы являетесь автором этого курса, поэтому не можете подписаться на него");
        }
        subscriptionService.subscribeUser(userFromDB, courseFromDB);
        return String.format("redirect:/courses/view/%d", courseFromDB.getId());
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/edit/{courseId}")
    public String renderEditCourse(Model model,
                                   @PathVariable Long courseId,
                                   @AuthenticationPrincipal PlatformUser user) {

        model.addAttribute("userProfileName", user.getProfile().getName());
        Course course = courseService.findCourse(courseId);

        if (!course.getAuthor().equals(user)) {
            throw new CourseEditingException("Вы не являетесь автором этого курса");
        }
        if (!course.getStatus().equals(CourseStatus.DRAFT)) {
            throw new CourseEditingException("Вы больше не имеете возможности редактировать этот курс");
        }

        FormCourse formCourse = conversionService.convert(course, FormCourse.class);
        model.addAttribute("tagCollection", tagService.findAll());
        model.addAttribute("course", formCourse);
        return "course/editCourse";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @PostMapping("/edit")
    public String editCourse(Model model,
                             @ModelAttribute("course") FormCourse course,
                             @AuthenticationPrincipal PlatformUser user,
                             BindingResult result) {
        course.setAuthor(user);
        Course courseFromDB = courseService.findCourse(course.getId());
        if (!courseFromDB.getAuthor().equals(user)) {
            throw new CourseEditingException("Вы не являетесь автором этого курса");
        }
        if (!courseFromDB.getStatus().equals(CourseStatus.DRAFT)) {
            throw new CourseEditingException("Вы больше не имеете возможности редактировать этот курс");
        }
        editValidator.validate(course, result);
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("tagCollection", tagService.findAll());
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "course/editCourse";
        }
        Course newCourse = conversionService.convert(course, Course.class);
        courseService.save(newCourse);
        return String.format("redirect:/courses/owned/%d", course.getId());
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/approve/request/{id}")
    public String submitCourseToApprove(Model model, @AuthenticationPrincipal PlatformUser user, @PathVariable Long id) {
        Course course = courseService.findCourse(id);
        if (!course.getAuthor().equals(user)) {
            throw new CourseSubmitException("Вы не можете отправить этот курс на проверку, так как не являетесь его автором");
        }
        courseService.submitToApprove(course, user);
        return "redirect:/courses/usersCourses";
    }


    @GetMapping("/search")
    public String courseSearch(Model model,
                               @AuthenticationPrincipal PlatformUser user,
                               @RequestParam(value = "name", required = false) String name) {

        if (user != null) {
            model.addAttribute("userProfileName", user.getProfile().getName());
        }
        if (name != null) {
            model.addAttribute("courses", courseService.findByPartName(name));
        }
        return "course/courseSearch";
    }


    @GetMapping("/search/tag")
    public String courseSearchByTag(Model model,
                                    @AuthenticationPrincipal PlatformUser user,
                                    @RequestParam(value = "name", required = false) String name) {
        if (user != null) {
            model.addAttribute("userProfileName", user.getProfile().getName());
        }
        if (name != null) {
            model.addAttribute("name", name);
            model.addAttribute("courses", courseService.findCoursesByTag(name));
        }
        model.addAttribute("tagCollection", tagService.findAll());
        return "course/courseSearchByTag";
    }


    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("/viewToApprove/{id}")
    public String viewCourseToApprove(Model model, @AuthenticationPrincipal PlatformUser user, @PathVariable Long id) {
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("course", courseService.findCourse(id));
        return "course/viewCourseToApprove";
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("/approve/{id}")
    public String approveCourse(@PathVariable Long id, @AuthenticationPrincipal PlatformUser user) {

        Course course = courseService.findCourse(id);
        courseService.approveCourse(course, course.getAuthor());
        return "redirect:/courses/newCourses";
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("/newCourses")
    public String showCoursesToAccept(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("courses", courseService.findByStatus(CourseStatus.AWAITING_CONFIRMATION));
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "course/showNewCourses";
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PostMapping("/deny/{id}")
    public String showCoursesToAccept(Model model, @AuthenticationPrincipal PlatformUser user,
                                      @PathVariable(name = "id") Long id,
                                      @RequestParam(name = "reason") String reason) {
        courseService.denyCourse(courseService.findCourse(id), reason);
        return "redirect:/courses/newCourses";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/archive/{id}")
    public String archiveCourse(@PathVariable Long id, @AuthenticationPrincipal PlatformUser user) {
        courseService.archiveCourseByCourseId(id);
        return "redirect:/courses/all";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/archived")
    public String archiveCourse(@AuthenticationPrincipal PlatformUser user, Model model) {
        model.addAttribute("courses", courseService.findByStatus(CourseStatus.ARCHIVED));
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "course/archivedCourses";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unarchive/{id}")
    public String unarchiveCourse(@AuthenticationPrincipal PlatformUser user, Model model, @PathVariable Long id) {
        courseService.unarchiveCourse(id);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "redirect:/courses/archived";
    }
}
