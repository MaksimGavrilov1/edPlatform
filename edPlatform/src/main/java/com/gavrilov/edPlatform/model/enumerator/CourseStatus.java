package com.gavrilov.edPlatform.model.enumerator;

public enum CourseStatus {
    DRAFT("В разработке"), AWAITING_CONFIRMATION("Ожидает подтверждения"), APPROVED("Одобрен");

    private final String value;

    CourseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
