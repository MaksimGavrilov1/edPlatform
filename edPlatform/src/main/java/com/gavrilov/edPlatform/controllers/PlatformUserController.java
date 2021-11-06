package com.gavrilov.edPlatform.controllers;

import com.gavrilov.edPlatform.models.PlatformUser;
import com.gavrilov.edPlatform.models.PlatformUserProfile;
import com.gavrilov.edPlatform.repositories.UserRepository;
import com.gavrilov.edPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public PlatformUser addUserProfile(@PathVariable Long id, @RequestBody PlatformUserProfile userProfile){
        return userService.addProfileInfo(userProfile,id);
    }

}
