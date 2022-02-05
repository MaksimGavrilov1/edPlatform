package com.gavrilov.edPlatform.service.courseThemeServiceImpl;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTheme;
import com.gavrilov.edPlatform.repo.CourseThemeRepository;
import com.gavrilov.edPlatform.service.CourseThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseThemeServiceImpl implements CourseThemeService {

    private final CourseThemeRepository courseThemeRepository;

    @Override
    public CourseTheme saveTheme(CourseTheme courseTheme) {
        return courseThemeRepository.save(courseTheme);
    }

    @Override
    public CourseTheme findTheme(Long id) {
        return courseThemeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTheme(Long id) {
        courseThemeRepository.deleteById(id);
    }

    @Override
    public List<CourseTheme> findThemesByCourse(Course course) {
        return courseThemeRepository.findByCourse(course).orElseGet(Collections::emptyList);
    }
}
