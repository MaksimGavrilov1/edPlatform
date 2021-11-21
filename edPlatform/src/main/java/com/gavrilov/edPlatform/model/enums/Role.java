package com.gavrilov.edPlatform.model.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"), MODERATOR("MODERATOR"), STUDENT("STUDENT"), TEACHER("TEACHER");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }
}
