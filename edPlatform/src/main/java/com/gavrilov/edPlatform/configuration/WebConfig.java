package com.gavrilov.edPlatform.configuration;

import com.gavrilov.edPlatform.converter.*;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.TagService;
import com.gavrilov.edPlatform.service.TestQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CourseService courseService;
    private final TagService tagService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FormThemeToCourseThemeConverter(courseService));
        registry.addConverter(new FormTestToThemeTestConverter(courseService));
        registry.addConverter(new CourseTestToTestDtoConverter());
        registry.addConverter(new TestDtoToTestResultDto(courseService));
        registry.addConverter(new UserDtoToPlatformUser());
        registry.addConverter(new FormCourseToCourseConverter(tagService));
        registry.addConverter(new CourseToFormCourse());
    }
}
