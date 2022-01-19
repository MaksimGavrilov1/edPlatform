package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.dto.UserRoleDto;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.UserRepository;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.ModeratorRoleRequestService;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("user")
public class PlatformUserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ModeratorRoleRequestService roleRequestService;
    private final AttemptService attemptService;

    @GetMapping("/profile")
    public String showUserProfile(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("user", user);
        model.addAttribute("attempts", attemptService.findByUser(user));
        return "showUserProfile";
    }

    @GetMapping("/editProfile")
    public String editUserProfile(Model model, @AuthenticationPrincipal PlatformUser user){
        if (user.getProfile() != null){
            model.addAttribute("userProfile", user.getProfile());
        } else {
            model.addAttribute("userProfile", new PlatformUserProfile());
        }
        return "editUserProfile";
    }

    @PostMapping("/editProfile")
    public String saveUserProfile (@ModelAttribute("userProfile") PlatformUserProfile profile,
                                   @AuthenticationPrincipal PlatformUser user,
                                   BindingResult result){
        user.setProfile(profile);
        userRepository.save(user);
        return "redirect:/user/profile";
    }

    @GetMapping("/moderatorRequest")
    public String showModeratorRequestPage (Model model){
        model.addAttribute("request", new ModeratorRoleRequest());
        return "showModeratorRequestPage";
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
