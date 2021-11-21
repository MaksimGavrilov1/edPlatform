package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/all")
    public String showCourses (Model model){
        model.addAttribute("courses", courseService.getAll());
        return "showCourses";
    }

//    @PostMapping("/addCourse")
//    public String addCourse (@RequestBody Course course){
//
//    }
}
