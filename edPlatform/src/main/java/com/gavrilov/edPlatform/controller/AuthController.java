package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.JwtRequest;
import com.gavrilov.edPlatform.model.JwtResponse;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.RefreshJwtRequest;
import com.gavrilov.edPlatform.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
