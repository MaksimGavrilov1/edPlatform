package com.gavrilov.edPlatform.controllers;

import com.gavrilov.edPlatform.jwt.JwtAuthentication;
import com.gavrilov.edPlatform.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloStudent() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("hello/teach")
    public ResponseEntity<String> helloTeacher() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello teach " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("hello/mod")
    public ResponseEntity<String> helloMod() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello mod " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello admin " + authInfo.getPrincipal() + "!");
    }
}
