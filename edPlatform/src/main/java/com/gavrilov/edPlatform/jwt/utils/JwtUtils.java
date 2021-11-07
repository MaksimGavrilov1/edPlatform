package com.gavrilov.edPlatform.jwt.utils;

import com.gavrilov.edPlatform.jwt.JwtAuthentication;
import com.gavrilov.edPlatform.models.enums.Role;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {


    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setLogin(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final String roles = claims.get("roles", String.class);
        Set<Role> setRole = new HashSet<>();
        setRole.add(Role.valueOf(roles));
        return setRole;
    }

}
