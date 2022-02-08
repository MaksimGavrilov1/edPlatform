package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTheme;

import java.util.List;

public interface CourseThemeService {

    CourseTheme saveTheme(CourseTheme courseTheme);

    CourseTheme findTheme(Long id);

    void deleteTheme(Long id);

    List<CourseTheme> findThemesByCourse(Course course);

}
