package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;

import java.util.List;
import java.util.Map;


public interface UserService {

    PlatformUser findByUsername(String login);

    PlatformUser findById(Long id);

    PlatformUser saveUser(PlatformUser user);

    List<PlatformUser> findAll();

    Map<PlatformUser, Integer> findModeratorsAndApprovedCoursesSize();

    Boolean compareAccessLevel(PlatformUser userToView, PlatformUser requester);

    void changePrivateInfo(PlatformUser user, PlatformUserProfile newProfile);

    void fillContent(boolean flag);
}
