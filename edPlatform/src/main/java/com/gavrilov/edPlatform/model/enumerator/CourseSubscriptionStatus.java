package com.gavrilov.edPlatform.model.enumerator;

public enum CourseSubscriptionStatus {
    CLOSED("Закрыт"), OPEN("Открыт"), EXPIRED("Время истекло")
    ;

    private final String value;

    CourseSubscriptionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
