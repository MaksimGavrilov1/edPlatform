package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.FormCourse;
import com.gavrilov.edPlatform.dto.TagDto;
import com.gavrilov.edPlatform.model.Course;
import org.springframework.core.convert.converter.Converter;

public class CourseToFormCourse implements Converter<Course, FormCourse> {
    @Override
    public FormCourse convert(Course source) {
        FormCourse formCourse = new FormCourse();
        formCourse.setId(source.getId());
        formCourse.setIsAlwaysOpen(source.getIsAlwaysOpen());
        formCourse.setAuthor(source.getAuthor());
        formCourse.setName(source.getName());
        formCourse.setDescription(source.getDescription());
        Integer days = Math.toIntExact(source.getActiveTime().getTime() / 1000 / 60 / 60 / 24);
        formCourse.setDays(days);
        formCourse.setIsAlwaysOpen(source.getIsAlwaysOpen());
        source.getTags().forEach(x->formCourse.getTags().add(new TagDto(x.getName(), true)));
        int size =formCourse.getTags().size();
        if (size < 3){
            for (int i=size;i<3;i++){
                formCourse.getTags().add(new TagDto());
            }
        }
        return formCourse;
    }
}
