package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;

import java.util.List;

public interface CourseService {
    Course save(Course course);

    public Course createCourse(Course course, Long aLong);

    public List<Course> getAll();

    public Course findCourse(Long id);

    List<Course> findCoursesByAuthor(PlatformUser user);

    List<Course> findCoursesAwaitingConfirmation();
}
