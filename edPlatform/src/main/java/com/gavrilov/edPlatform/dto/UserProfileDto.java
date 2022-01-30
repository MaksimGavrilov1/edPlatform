package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {

    private String name;
    private String surname;
    private String middleName;
    private String selfDescription;
    private String role;
    private Long subscriptionsAmount;
    private Long createdCoursesAmount;
    private Integer testSuccessRate;

    @Override
    public String toString() {
        return String.format("%s %s %s", surname, name, middleName);
    }
}
