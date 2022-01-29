package com.gavrilov.edPlatform.service.userServiceImpl;

import com.gavrilov.edPlatform.exception.UserNotFoundException;
import com.gavrilov.edPlatform.exception.UserProfileNotValidException;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.PlatformUserProfileRepository;
import com.gavrilov.edPlatform.repo.UserRepository;
import com.gavrilov.edPlatform.service.CourseConfirmationRequestService;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final PlatformUserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final CourseConfirmationRequestService courseConfirmationRequestService;
   // private final Validator validator;


    @Override
    public PlatformUser addProfileInfo(@Validated PlatformUserProfile userProfile, Long id) {



//        Set<ConstraintViolation<PlatformUserProfile>> violations = validator.validate(userProfile);
//
//        if (!violations.isEmpty()) {
//            StringBuilder sb = new StringBuilder();
//            for (ConstraintViolation<PlatformUserProfile> constraintViolation : violations) {
//                sb.append(constraintViolation.getMessage());
//            }
//            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
//        }

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
    public PlatformUser findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public PlatformUser findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public PlatformUser saveUser(PlatformUser user) {
        return userRepository.save(user);
    }

    @Override
    public List<PlatformUser> findAll() {
        return userRepository.findAll();
    }


    private List<PlatformUser> findByRole(Role role) {
        return userRepository.findByRole(role).orElseGet(Collections::emptyList);
    }

    @Override
    public Map<PlatformUser, Integer> findModeratorsAndApprovedCoursesSize() {
        Map<PlatformUser,Integer> result = new HashMap<>();
        List<PlatformUser> moderators = findByRole(Role.MODERATOR);
        moderators.forEach(x->result.put(x, courseConfirmationRequestService.findByUser(x).size()));
        return result;
    }


}
