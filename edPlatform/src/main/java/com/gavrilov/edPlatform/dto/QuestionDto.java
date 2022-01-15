package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDto {

    private Long id;
    private String title;
    private List<AnswerDto> answers = new ArrayList<>();
    private Integer rightAnswerAmount;


}
