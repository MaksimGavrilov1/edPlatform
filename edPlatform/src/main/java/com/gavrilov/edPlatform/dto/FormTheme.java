package com.gavrilov.edPlatform.dto;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTheme;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormTheme {

    private Long id;
    private String name;
    private String description;
    private Long courseId;

}
