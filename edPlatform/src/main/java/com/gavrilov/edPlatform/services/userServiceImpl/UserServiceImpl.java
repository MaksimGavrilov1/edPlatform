package com.gavrilov.edPlatform.services.userServiceImpl;

import com.gavrilov.edPlatform.exceptions.UserNotFoundException;
import com.gavrilov.edPlatform.exceptions.UserProfileNotValidException;
import com.gavrilov.edPlatform.models.PlatformUser;
import com.gavrilov.edPlatform.models.PlatformUserProfile;
import com.gavrilov.edPlatform.repositories.PlatformUserProfileRepository;
import com.gavrilov.edPlatform.repositories.UserRepository;
import com.gavrilov.edPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import java.util.Set;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final PlatformUserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final Validator validator;


    @Override
    public PlatformUser addProfileInfo(@Validated PlatformUserProfile userProfile, Long id) {



        Set<ConstraintViolation<PlatformUserProfile>> violations = validator.validate(userProfile);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<PlatformUserProfile> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        PlatformUser userDB = userRepository.findById(id).orElse(null);
        if (userProfile == null) {
            log.error("IN addProfileInfo: user profile is null");
            throw new UserProfileNotValidException("Invalid user profile");
        }
        if (userDB != null) {
            //save profile in database and get profile entity with id
            PlatformUserProfile profile = userProfileRepository.save(userProfile);
            userDB.setProfile(profile);
            userRepository.save(userDB);
            profile.setPlatformUser(userDB);
            userProfileRepository.save(profile);

            log.info(String.format("IN addProfileInfo: added profile info to user - %d", id));
            return userDB;
        } else {
            log.error(String.format("IN addProfileInfo: no user found with id %d", id));
            throw new UserNotFoundException("User with this login doesn't exists");
        }
        //exception and handler , userProfileNotValidException

    }

    @Override
    public PlatformUser findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    @Override
    public PlatformUser saveUser(PlatformUser user) {
        return userRepository.save(user);
    }
}
