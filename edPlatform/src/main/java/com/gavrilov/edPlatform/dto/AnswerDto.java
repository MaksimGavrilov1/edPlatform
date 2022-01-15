package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {

    private Long id;
    private String title;
    private Long questionId;
    private Boolean checked;

}
