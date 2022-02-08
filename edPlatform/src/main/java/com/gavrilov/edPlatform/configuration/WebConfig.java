package com.gavrilov.edPlatform.configuration;

import com.gavrilov.edPlatform.converter.*;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.SubscriptionService;
import com.gavrilov.edPlatform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CourseService courseService;
    private final SubscriptionService subscriptionService;
    private final AttemptService attemptService;
    private final TagService tagService;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FormThemeToCourseThemeConverter(courseService));
        registry.addConverter(new FormTestToThemeTestConverter(courseService));
        registry.addConverter(new CourseTestToTestDtoConverter());

        registry.addConverter(new UserDtoToPlatformUser());
        registry.addConverter(new FormCourseToCourseConverter(tagService));
        registry.addConverter(new CourseToFormCourse());
        registry.addConverter(new PlatformUserToUserProfileDtoConverter(courseService, subscriptionService, attemptService));
    }
}
