package com.gavrilov.edPlatform.services;

import com.gavrilov.edPlatform.models.PlatformUser;
import com.gavrilov.edPlatform.models.PlatformUserProfile;
import org.springframework.stereotype.Service;


public interface UserService {
    public PlatformUser addProfileInfo (PlatformUserProfile userProfile, Long id);
}
