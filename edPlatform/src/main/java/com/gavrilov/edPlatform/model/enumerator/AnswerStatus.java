package com.gavrilov.edPlatform.model.enumerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerStatus {
    CHOSEN_RIGHT("CHOSEN_RIGHT"), CHOSEN_WRONG("CHOSEN_WRONG"), NOT_CHOSEN("NOT_CHOSEN");

    private final String value;


    public String getValue() {
        return value;
    }
}
