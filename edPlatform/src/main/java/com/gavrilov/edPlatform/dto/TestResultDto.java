package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TestResultDto {

    private String name;
    private List<QuestionResultDto> questions = new ArrayList<>();
    private Integer mark;

}
