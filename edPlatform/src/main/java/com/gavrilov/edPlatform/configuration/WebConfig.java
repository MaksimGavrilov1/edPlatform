package com.gavrilov.edPlatform.configuration;

import com.gavrilov.edPlatform.converter.CourseTestToTestDtoConverter;
import com.gavrilov.edPlatform.converter.FormTestToThemeTestConverter;
import com.gavrilov.edPlatform.converter.FormThemeToCourseThemeConverter;
import com.gavrilov.edPlatform.converter.TestDtoToTestResultDto;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.TestQuestionService;
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
        registry.addConverter(new FormTestToThemeTestConverter(courseService));
        registry.addConverter(new CourseTestToTestDtoConverter());
        registry.addConverter(new TestDtoToTestResultDto(courseService));
    }
}
