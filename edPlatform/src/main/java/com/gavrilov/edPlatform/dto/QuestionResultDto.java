package com.gavrilov.edPlatform.dto;

import com.gavrilov.edPlatform.model.UserAnswer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionResultDto {

    private Long id;
    private String title;
    private List<UserAnswer> answers = new ArrayList<>();
    private Integer rightAnswerAmount;

}
