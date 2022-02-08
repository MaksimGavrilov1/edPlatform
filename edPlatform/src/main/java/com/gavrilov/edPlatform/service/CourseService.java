package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;

import java.util.List;

public interface CourseService {
    Course save(Course course);

    List<Course> getAll();

    Course findCourse(Long id);

    List<Course> findCoursesByAuthor(PlatformUser user);

    List<Course> findCoursesWithEmptyTestByAuthor(PlatformUser user);

    List<Course> findTenMostPopular(PlatformUser user);

    List<Course> findTenNewest(PlatformUser user);

    List<Course> findByPartName(String partName);

    List<Course> findCoursesByTag(String tag);

    List<Course> findByStatus(CourseStatus status);

    List<Course> findByAuthorAndStatus(PlatformUser user, CourseStatus status);

    Long countByAuthor(PlatformUser author);

    boolean isValidToApprove(Course course);

    void denyCourse(Course course, String reason);

    void approveCourse(Course course, PlatformUser user);

    void submitToApprove(Course course, PlatformUser user);

    void archiveCourseByCourseId(Long id);

    void unarchiveCourse(Long id);
}
