package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionResultDto {

    private Long id;
    private String title;
    private List<AnswerResultDto> answers = new ArrayList<>();
    private Integer rightAnswerAmount;

}
