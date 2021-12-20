package com.gavrilov.edPlatform.converter;


import com.gavrilov.edPlatform.dto.FormTheme;
import com.gavrilov.edPlatform.model.CourseTheme;
import com.gavrilov.edPlatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;

@RequiredArgsConstructor
public class FormThemeToCourseThemeConverter implements Converter<FormTheme, CourseTheme> {

    private final CourseService courseService;

    @Override
    public CourseTheme convert(FormTheme source) {
        CourseTheme ct = new CourseTheme();
        ct.setId(source.getId());
        ct.setName(source.getName());
        ct.setLectureMaterial(source.getDescription());
        ct.setCourse(courseService.findCourse(source.getCourseId()));

        return ct;
    }
}
