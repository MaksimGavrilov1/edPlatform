package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;


public interface UserService {
    public PlatformUser addProfileInfo (PlatformUserProfile userProfile, Long id);

    public PlatformUser findByUsername(String login);

    public PlatformUser saveUser(PlatformUser user);
}
