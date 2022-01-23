package com.gavrilov.edPlatform.service.courseThemeServiceImpl;

import com.gavrilov.edPlatform.model.CourseTheme;
import com.gavrilov.edPlatform.repo.CourseThemeRepository;
import com.gavrilov.edPlatform.service.CourseThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
