package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormTest {

    private Long id;
    private String name;
    private Long courseId;
    private Integer questionAmount;
    private Integer amountOfAttempts;
    private Integer minThreshold;
}
