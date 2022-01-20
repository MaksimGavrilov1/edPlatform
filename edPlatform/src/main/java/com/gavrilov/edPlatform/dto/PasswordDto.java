package com.gavrilov.edPlatform.dto;

import com.gavrilov.edPlatform.model.PlatformUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {

    private PlatformUser user;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

}
