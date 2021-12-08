package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTheme;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.repo.CourseThemeRepository;
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

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final CourseValidator validator;
    private final CourseThemeValidator courseThemeValidator;
    private final CourseThemeRepository courseThemeRepository;

    @GetMapping("/all")
    public String showCourses (Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("usersAmount", userService.findAll().size());
        model.addAttribute("user", user);
        model.addAttribute("courses", courseService.getAll());
        return "showCourses";
    }

    @GetMapping("/addCourse")
    public String addCourse (Model model){
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
        //ploho?
        return String.format("redirect:/courses/addThemes/%d", c.getId());
    }

    @GetMapping("/addThemes/{courseId}")
    public String addThemes (@PathVariable(name = "courseId") Long courseId, Model model){
        Course course= courseService.findCourse(courseId);
        model.addAttribute("themes", course.getThemes());
        CourseTheme courseTheme = new CourseTheme();
        courseTheme.setCourse(course);
        model.addAttribute("mainTheme", courseTheme);
        return "addThemes";
    }

    @PostMapping("/addTheme/{courseId}")
    public String addTheme (@PathVariable Long courseId,
                            @ModelAttribute CourseTheme theme,
                            Model model,
                            BindingResult result) {

        Course c = courseService.findCourse(courseId);
        theme.setCourse(c);

        courseThemeValidator.validate(theme, result);
        if (result.hasErrors()) {
            return "addThemes";
        }

        CourseTheme ct = courseThemeRepository.save(theme);

        return String.format("redirect:/courses/addThemes/%d", courseId);
    }

    @GetMapping("/testPage")
    public String showTestPage (Model model){
        return "testPage";
    }
}
