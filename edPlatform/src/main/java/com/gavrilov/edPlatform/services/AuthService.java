package com.gavrilov.edPlatform.services;

import com.gavrilov.edPlatform.jwt.JwtAuthentication;
import com.gavrilov.edPlatform.models.JwtRequest;
import com.gavrilov.edPlatform.models.JwtResponse;
import com.gavrilov.edPlatform.models.PlatformUser;
import lombok.NonNull;

import javax.security.auth.message.AuthException;

public interface AuthService {

    public JwtResponse login(@NonNull JwtRequest authRequest);

    public PlatformUser register(@NonNull PlatformUser user);

    public JwtResponse getAccessToken(@NonNull String refreshToken);

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException;

    public JwtAuthentication getAuthInfo();
}
