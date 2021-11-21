package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.dto.UserRoleDto;
import com.gavrilov.edPlatform.model.enums.Role;
import com.gavrilov.edPlatform.repository.UserRepository;
import com.gavrilov.edPlatform.service.UserService;
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
