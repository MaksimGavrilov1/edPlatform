package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("course")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping("/all")
    public List<Course> getAll(){
        return courseService.getAll();
    }

    @PostMapping("/addCourse")
    public Course addCourse(@RequestBody Course course){
        return courseService.createCourse(course, 1L);
    }

}
