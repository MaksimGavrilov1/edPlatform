package com.gavrilov.edPlatform.model.enumerator;

public enum RequestStatus {
    PENDING("Обрабатывается"), APPROVED("Разрешено"), DECLINED("Отказано")

    ;

    private final String value;

    RequestStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
