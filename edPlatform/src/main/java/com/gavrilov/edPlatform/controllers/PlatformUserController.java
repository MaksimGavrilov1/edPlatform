package com.gavrilov.edPlatform.controllers;

import com.gavrilov.edPlatform.models.PlatformUser;
import com.gavrilov.edPlatform.models.PlatformUserProfile;
import com.gavrilov.edPlatform.models.UserRoleDto;
import com.gavrilov.edPlatform.models.enums.Role;
import com.gavrilov.edPlatform.repositories.UserRepository;
import com.gavrilov.edPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("user")
public class PlatformUserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/all")
    public List<PlatformUser> getUsers (){
        return userRepository.findAll();
    }

    @PostMapping("/add")
    public PlatformUser addUser(@RequestBody PlatformUser user){
        return userRepository.save(user);
    }

    @PutMapping ("/addInfo/{id}")
    public PlatformUser addUserProfile(@PathVariable Long id, @RequestBody @Valid PlatformUserProfile userProfile){
        return userService.addProfileInfo(userProfile,id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/editUser/{id}")
    public PlatformUser editUserRole (@PathVariable Long id, @RequestBody UserRoleDto role){
        PlatformUser user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("No user with this id"));
        user.setRole(Role.valueOf(role.getRole()));
        return userRepository.save(user);
    }

}
