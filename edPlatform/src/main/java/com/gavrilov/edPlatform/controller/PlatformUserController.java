package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.PasswordDto;
import com.gavrilov.edPlatform.dto.UserProfileDto;
import com.gavrilov.edPlatform.exception.AccessProfileException;
import com.gavrilov.edPlatform.exception.RoleChangeException;
import com.gavrilov.edPlatform.exception.UnauthorizedUserException;
import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.ModeratorRoleRequestService;
import com.gavrilov.edPlatform.service.SubscriptionService;
import com.gavrilov.edPlatform.service.UserService;
import com.gavrilov.edPlatform.validator.PasswordDtoValidator;
import com.gavrilov.edPlatform.validator.PlatformUserProfileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
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
    private final ModeratorRoleRequestService moderatorRoleRequestService;
    private final ConversionService conversionService;




    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showUserProfile(Model model, @AuthenticationPrincipal PlatformUser user) {


        subscriptionService.updateSubscriptionStatus(user);
        UserProfileDto userProfileDto = conversionService.convert(user, UserProfileDto.class);
        model.addAttribute("user", userProfileDto);
        model.addAttribute("subs", subscriptionService.findByUser(user));
        model.addAttribute("attempts", attemptService.findLastTenAttempts(user));
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "user/showUserProfile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editProfile")
    public String editUserProfile(Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("userProfile", user.getProfile());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "user/editUserProfile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editProfile")
    public String saveUserProfile (@ModelAttribute("userProfile") PlatformUserProfile profile,
                                   @AuthenticationPrincipal PlatformUser user,
                                   BindingResult result,
                                   Model model){
        profileValidator.validate(profile, result);
        if(result.hasErrors()){
            model.addAttribute("userProfile", profile);
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "user/editUserProfile";
        }
        user.setProfile(profile);
        model.addAttribute("userProfileName", user.getProfile().getName());
        userService.saveUser(user);
        return "redirect:/user/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/changePassword")
    public String renderChangePasswordPage(Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("formPassword", new PasswordDto());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "user/changePassword";
    }

    @PreAuthorize("isAuthenticated()")
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
            return "user/changePassword";
        }
        userFromDB.setPassword(encoder.encode(formPassword.getNewPassword()));
        userService.saveUser(userFromDB);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "user/successfulPasswordChange";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/moderatorRequest")
    public String showModeratorRequestPage (Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("request", new ModeratorRoleRequest());
        model.addAttribute("requests", moderatorRoleRequestService.findByUser(user));
        model.addAttribute("isApprovedRequest", moderatorRoleRequestService.isUserHaveApproved(user));
        model.addAttribute("isActiveRequest", moderatorRoleRequestService.isUserHaveAnyActiveRequest(user));
        model.addAttribute("declined", RequestStatus.DECLINED);
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "user/student_teacher/moderatorRequestPage";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @PostMapping("/moderatorRequest")
    public String saveModeratorRequest (@ModelAttribute("request") ModeratorRoleRequest request,
                                        @AuthenticationPrincipal PlatformUser user,
                                        BindingResult result){
        request.setUser(user);
        roleRequestService.save(request);
        return "redirect:/courses/all";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/view/profile/{id}")
    public String viewUserProfile(@PathVariable Long id, @AuthenticationPrincipal PlatformUser user, Model model){
        if (user == null){
            throw new UnauthorizedUserException("Для того чтобы просматривать профили других пользователей, вам необходимо зарегистрироваться");
        }
        PlatformUser userToView = userService.findById(id);
        if (userService.compareAccessLevel(userToView, user)){
            UserProfileDto profileDto = conversionService.convert(userToView, UserProfileDto.class);
            model.addAttribute("userProfileName", user.getProfile().getName());
            model.addAttribute("user", profileDto);
            return "user/viewProfile";
        } else {
            throw new AccessProfileException("Вы не можете просматривать профиль этого пользователя");
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/moderators")
    public String showModerators(Model model, @AuthenticationPrincipal PlatformUser user){

        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("moderators", userService.findModeratorsAndApprovedCoursesSize());
        return "user/admin/moderatorsList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/newModerators")
    public String showModeratorRequests(Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("requests", moderatorRoleRequestService.findByStatus(RequestStatus.PENDING));
        return "user/admin/newModerators";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/moderator/approve/{id}")
    public String approveModerator (Model model, @AuthenticationPrincipal PlatformUser user,
                                    @PathVariable(name = "id") Long id){
        moderatorRoleRequestService.approveUser(userService.findById(id));
        return "redirect:/user/newModerators";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/moderator/deny/{id}")
    public String denyModerator(Model model, @AuthenticationPrincipal PlatformUser user,
                                @PathVariable(name="id") Long id,
                                @RequestParam(name = "reason") String reason){
        moderatorRoleRequestService.denyUser(userService.findById(id), reason);
        return "redirect:/user/newModerators";
    }

    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    @GetMapping("/moderator/ready")
    public String readyToChangeRole(@AuthenticationPrincipal PlatformUser user){
        if (moderatorRoleRequestService.isUserHaveApproved(user)){
            PlatformUser userFromDB = userService.findByUsername(user.getUsername());
            userFromDB.setRole(Role.MODERATOR);
            PlatformUser newUser = userService.saveUser(userFromDB);
            return "redirect:/logout";
        } else {
            throw new RoleChangeException("Ваша заявка на роль модератора еще не была одобрена");
        }
    }
}
