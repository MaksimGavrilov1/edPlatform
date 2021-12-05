package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.CourseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final CourseValidator validator;

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
    public String sendCourse(@ModelAttribute Course course, BindingResult result, @AuthenticationPrincipal PlatformUser user) {
        course.setAuthor(user);
        validator.validate(course, result);
        System.out.println("in ADDcourse post");
        if (result.hasErrors()) {
            System.out.println("error asdasdasd");
            return "addCourse";
        }

        courseService.createCourse(course, user.getId());
        return "redirect:/addThemes";
    }

    @GetMapping("/addThemes")
    public String addThemes (Model model){

        return "addThemes";
    }
}
