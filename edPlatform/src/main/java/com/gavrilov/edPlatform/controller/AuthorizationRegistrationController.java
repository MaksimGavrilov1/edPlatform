package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.PlatformUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthorizationRegistrationController {

    private final UserService userService;
    private final PlatformUserValidator userValidator;
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/login")
    public String login(Model model, String error, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("error", error);

        return "login";
    }


    @GetMapping("/register")
    public String registration(Model model, @AuthenticationPrincipal PlatformUser user) {

        if (user != null) {
            return "redirect:/courses/all";
        }

        PlatformUser user1 = new PlatformUser();
        user1.setProfile(new PlatformUserProfile());
        model.addAttribute("user", user1);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(value = "user")PlatformUser user, BindingResult result){
        userValidator.validate(user, result);

        if (result.hasErrors()){
            return "register";
        }

        String userPassword = user.getPassword();
        user.setPassword(encoder.encode(userPassword));
        return "redirect:/login";
    }
}