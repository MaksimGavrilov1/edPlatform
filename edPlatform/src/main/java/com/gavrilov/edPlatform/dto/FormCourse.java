package com.gavrilov.edPlatform.dto;

import com.gavrilov.edPlatform.model.PlatformUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormCourse {

    private Long id;
    private String name;
    private String description;
    private Integer days;
    private Boolean isAlwaysOpen;
    private PlatformUser author;

}
