package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.PasswordDto;
import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.ModeratorRoleRequestService;
import com.gavrilov.edPlatform.service.SubscriptionService;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.PasswordDtoValidator;
import com.gavrilov.edPlatform.validator.PlatformUserProfileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("user")
public class PlatformUserController {

    private final UserService userService;
    private final ModeratorRoleRequestService roleRequestService;
    private final AttemptService attemptService;
    private final PasswordDtoValidator passwordDtoValidator;
    private final BCryptPasswordEncoder encoder;
    private final PlatformUserProfileValidator profileValidator;
    private final SubscriptionService subscriptionService;



    @GetMapping("/profile")
    public String showUserProfile(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("user", user);
        subscriptionService.updateSubscriptionStatus(user);
        model.addAttribute("subs", subscriptionService.findByUser(user));
        model.addAttribute("attempts", attemptService.findLastTenAttempts(user));
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "showUserProfile";
    }

    @GetMapping("/editProfile")
    public String editUserProfile(Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("userProfile", user.getProfile());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "editUserProfile";
    }

    @PostMapping("/editProfile")
    public String saveUserProfile (@ModelAttribute("userProfile") PlatformUserProfile profile,
                                   @AuthenticationPrincipal PlatformUser user,
                                   BindingResult result,
                                   Model model){
        profileValidator.validate(profile, result);
        if(result.hasErrors()){
            model.addAttribute("userProfile", profile);
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "editUserProfile";
        }
        user.setProfile(profile);
        model.addAttribute("userProfileName", user.getProfile().getName());
        userService.saveUser(user);
        return "redirect:/user/profile";
    }

    @GetMapping("/changePassword")
    public String renderChangePasswordPage(Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("formPassword", new PasswordDto());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute("formPassword") PasswordDto formPassword,
                                 @AuthenticationPrincipal PlatformUser user,
                                 BindingResult result,
                                 Model model){
        formPassword.setUser(user);
        passwordDtoValidator.validate(formPassword, result);
        PlatformUser userFromDB = userService.findByUsername(user.getUsername());
        if (result.hasErrors()){
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "changePassword";
        }
        userFromDB.setPassword(encoder.encode(formPassword.getNewPassword()));
        userService.saveUser(userFromDB);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "successfulPasswordChange";
    }

    @GetMapping("/moderatorRequest")
    public String showModeratorRequestPage (Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("request", new ModeratorRoleRequest());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "moderatorRequestPage";
    }

    @PostMapping("/moderatorRequest")
    public String saveModeratorRequest (@ModelAttribute("request") ModeratorRoleRequest request,
                                        @AuthenticationPrincipal PlatformUser user,
                                        BindingResult result){
        request.setUser(user);
        roleRequestService.save(request);
        return "redirect:/courses/all";
    }

}
