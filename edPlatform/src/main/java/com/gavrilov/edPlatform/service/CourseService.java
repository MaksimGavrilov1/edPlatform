package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Course;

import java.util.List;

public interface CourseService {
    public Course createCourse(Course course, Long aLong);

    public List<Course> getAll();

    public Course findCourse(Long id);
}
