package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.FormTest;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class FormTestToThemeTestConverter implements Converter<FormTest, CourseTest> {

    private final CourseService courseService;


    @Override
    public CourseTest convert(FormTest source) {

        Course course = courseService.findCourse(source.getCourseId());
        CourseTest test = new CourseTest();
        test.setCourse(course);
        test.setName(source.getName());
        test.setAmountOfAttempts(source.getAmountOfAttempts());
        test.setMinThreshold(source.getMinThreshold());

        return test;
    }
}
