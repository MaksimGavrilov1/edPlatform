package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TestDto {

    private Long courseId;
    private String name;
    private List<QuestionDto> questions = new ArrayList<>();

}
