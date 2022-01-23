package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.FormCourse;
import com.gavrilov.edPlatform.model.Course;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;

public class FormCourseToCourseConverter implements Converter<FormCourse, Course> {

    @Override
    public Course convert(FormCourse source) {
        Course course = new Course();
        course.setName(source.getName());
        course.setAuthor(source.getAuthor());
        course.setDescription(source.getDescription());
        course.setIsAlwaysOpen(source.getIsAlwaysOpen());
        long time = source.getDays() * 24 * 60 * 60 * 1000;
        course.setActiveTime(new Timestamp(time));
        return course;
    }
}
