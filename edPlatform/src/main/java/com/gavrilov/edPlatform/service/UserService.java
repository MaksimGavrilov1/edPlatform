package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;

import java.util.List;
import java.util.Map;


public interface UserService {
    public PlatformUser addProfileInfo (PlatformUserProfile userProfile, Long id);

    public PlatformUser findByUsername(String login);

    PlatformUser findById(Long id);

    public PlatformUser saveUser(PlatformUser user);

    public List<PlatformUser> findAll();

   // List<PlatformUser> findByRole(Role role);

    Map<PlatformUser, Integer> findModeratorsAndApprovedCoursesSize();

    Boolean compareAccessLevel(PlatformUser userToView, PlatformUser requester);
}
