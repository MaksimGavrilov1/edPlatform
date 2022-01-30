package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Tag;

import java.util.List;

public interface CourseService {
    Course save(Course course);

    public Course createCourse(Course course, Long aLong);

    public List<Course> getAll();

    public Course findCourse(Long id);

    List<Course> findCoursesByAuthor(PlatformUser user);

    List<Course> findCoursesAwaitingConfirmation();

    List<Course> findCoursesWithEmptyTestByAuthor(PlatformUser user);

    List<Course> findTenMostPopular();

    List<Course> findTenNewest();

    List<Course> findByPartName(String partName);

    List<Course> findCoursesByTag(String tag);

    Long countByAuthor(PlatformUser author);

    void denyCourse(Course course, String reason);

    void approveCourse(Course course, PlatformUser user);

    void submitToApprove(Course course, PlatformUser user);

    void archiveCourseByCourseId(Long id);
}
