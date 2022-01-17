package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String username;
    private String name;
    private String surname;
    private String middleName;
    private String selfDescription;
    private String password;
    private String confirmPassword;

}
