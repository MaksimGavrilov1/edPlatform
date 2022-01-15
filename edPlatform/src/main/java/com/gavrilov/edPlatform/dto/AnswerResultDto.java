package com.gavrilov.edPlatform.dto;

import com.gavrilov.edPlatform.model.enumerator.AnswerStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResultDto {

    private Long questionId;
    private String text;
    private AnswerStatus status = AnswerStatus.NOT_CHOSEN;

}
