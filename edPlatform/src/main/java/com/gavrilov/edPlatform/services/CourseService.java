package com.gavrilov.edPlatform.services;

import com.gavrilov.edPlatform.models.Course;

import java.util.List;

public interface CourseService {
    public Course createCourse(Course course, Long aLong);

    public List<Course> getAll();
}
