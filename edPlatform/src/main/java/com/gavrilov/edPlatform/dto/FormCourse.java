package com.gavrilov.edPlatform.dto;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FormCourse {

    private Long id;
    private String name;
    private String description;
    private Integer days;
    private Boolean isAlwaysOpen;
    private PlatformUser author;
    private List<TagDto> tags = new ArrayList<>();

}
