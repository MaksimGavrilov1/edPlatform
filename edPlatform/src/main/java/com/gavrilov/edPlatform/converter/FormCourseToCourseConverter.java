package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.FormCourse;
import com.gavrilov.edPlatform.dto.TagDto;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.Tag;
import com.gavrilov.edPlatform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FormCourseToCourseConverter implements Converter<FormCourse, Course> {

    private final TagService tagService;

    @Override
    public Course convert(FormCourse source) {
        Course course = new Course();
        course.setId(source.getId());
        course.setName(source.getName());
        course.setAuthor(source.getAuthor());
        course.setDescription(source.getDescription());
        course.setIsAlwaysOpen(source.getIsAlwaysOpen());
        long time = source.getDays() * 24 * 60 * 60 * 1000;
        course.setActiveTime(new Timestamp(time));
        course.setTags(new ArrayList<>());
        source.getTags().stream()
                .map(x->tagService.findByName(x.getName()))
                .forEach(x->course.getTags().add(x));

        return course;
    }
}
