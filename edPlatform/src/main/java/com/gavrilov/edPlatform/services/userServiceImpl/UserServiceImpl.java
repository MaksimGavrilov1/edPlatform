package com.gavrilov.edPlatform.services.userServiceImpl;

import com.gavrilov.edPlatform.models.PlatformUser;
import com.gavrilov.edPlatform.models.PlatformUserProfile;
import com.gavrilov.edPlatform.repositories.PlatformUserProfileRepository;
import com.gavrilov.edPlatform.repositories.UserRepository;
import com.gavrilov.edPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final PlatformUserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Override
    public PlatformUser addProfileInfo(PlatformUserProfile userProfile, Long id){

        PlatformUser userDB = userRepository.findById(id).orElse(null);

        if (userDB != null) {
            userDB.setProfile(userProfile);
            userRepository.save(userDB);

            log.info(String.format("IN addProfileInfo: added profile info to user - %d",id));

            return userDB;
        }
       //как лучше сделать

        log.error(String.format("IN addProfileInfo: no user found with id %d", id));

        return null;
    }
}
