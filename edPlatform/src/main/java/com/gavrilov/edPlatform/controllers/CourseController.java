package com.gavrilov.edPlatform.controllers;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.gavrilov.edPlatform.models.Course;
import com.gavrilov.edPlatform.repositories.CourseRepository;
import com.gavrilov.edPlatform.services.CourseService;
import com.gavrilov.edPlatform.services.courseServiceImpl.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/all")
    public List<Course> getAll(){
        return courseService.getAll();
    }

    @PostMapping("/addCourse")
    public Course addCourse(@RequestBody Course course){
        return courseService.createCourse(course, Long.valueOf(1));
    }

}
