package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.PlatformUserProfileRepository;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.PlatformUserValidator;
import com.gavrilov.edPlatform.validator.UserDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthorizationRegistrationController {

    private final UserService userService;
    private final PlatformUserValidator userValidator;
    private final UserDtoValidator userDtoValidator;
    private final BCryptPasswordEncoder encoder;
    private final ConversionService conversionService;
    private final PlatformUserProfileRepository platformUserProfileRepository;
    boolean flag = false;

    @GetMapping("/login")
    public String login(Model model, String error, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("error", error);
        if (!flag){
            PlatformUser admin = new PlatformUser();
            admin.setRole(Role.ADMIN);
            admin.setUsername("admin");
            admin.setPassword("qwerty");
            userService.saveUser(admin);
            flag = true;
        }

        if (user != null) {
            return "redirect:/courses/all";
        }
        return "login";
    }


    @GetMapping("/register")
    public String registration(Model model, @AuthenticationPrincipal PlatformUser user) {

        if (user != null) {
            return "redirect:/courses/all";
        }

        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(value = "userDto")UserDto userDto, BindingResult result){
        userDtoValidator.validate(userDto, result);
        if (result.hasErrors()){
            return "register";
        }
        PlatformUser user = conversionService.convert(userDto, PlatformUser.class);
        String userPassword = Objects.requireNonNull(user).getPassword();
        user.setPassword(encoder.encode(userPassword));
        PlatformUser newUser = userService.saveUser(user);
        return "redirect:/login";
    }
}
