package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.configuration.UserDetailServiceImpl;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.PlatformUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller("/")
@RequiredArgsConstructor
public class AuthorizationRegistrationController {

    private final UserService userService;
    private final PlatformUserValidator userValidator;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String registration(Model model, @AuthenticationPrincipal PlatformUser user) {

        if (user != null) {
            return "redirect:/courses/all";
        }

        model.addAttribute("user", new PlatformUser());

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(value = "user")PlatformUser user, BindingResult result){
        userValidator.validate(user, result);

        if (result.hasErrors()){
            return "register";
        }

        userService.saveUser(user);
        return "redirect:/login";
    }
}
