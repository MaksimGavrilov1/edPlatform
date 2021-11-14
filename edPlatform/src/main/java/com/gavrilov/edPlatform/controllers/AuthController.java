package com.gavrilov.edPlatform.controllers;

import com.gavrilov.edPlatform.models.JwtRequest;
import com.gavrilov.edPlatform.models.JwtResponse;
import com.gavrilov.edPlatform.models.PlatformUser;
import com.gavrilov.edPlatform.models.RefreshJwtRequest;
import com.gavrilov.edPlatform.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token;
        token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }



    @PostMapping("register")
    public PlatformUser register(@RequestBody PlatformUser user){
        return authService.register(user);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @SneakyThrows
    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
