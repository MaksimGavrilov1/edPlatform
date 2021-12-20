package com.gavrilov.edPlatform.configuration;

import com.gavrilov.edPlatform.converter.FormThemeToCourseThemeConverter;
import com.gavrilov.edPlatform.repo.CourseRepository;
import com.gavrilov.edPlatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CourseService courseService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FormThemeToCourseThemeConverter(courseService));
    }
}
