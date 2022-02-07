package com.gavrilov.edPlatform.service.userServiceImpl;

import com.gavrilov.edPlatform.exception.ResourceNotFoundException;
import com.gavrilov.edPlatform.model.*;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.CourseRepository;
import com.gavrilov.edPlatform.repo.PlatformUserProfileRepository;
import com.gavrilov.edPlatform.repo.UserRepository;
import com.gavrilov.edPlatform.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final PlatformUserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final CourseConfirmationRequestService courseConfirmationRequestService;
    private final PlatformUserProfileRepository platformUserProfileRepository;
    private final CourseRepository courseRepository;
    private final CourseThemeService courseThemeService;
    private final ThemeTestService testService;
    private final TestQuestionService testQuestionService;
    private final QuestionStandardAnswerService questionStandardAnswerService;
    private final TagService tagService;
    private final BCryptPasswordEncoder encoder;


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
        Map<PlatformUser, Integer> result = new HashMap<>();
        List<PlatformUser> moderators = findByRole(Role.MODERATOR);
        moderators.forEach(x -> result.put(x, courseConfirmationRequestService.findByUser(x).size()));
        return result;
    }

    @Override
    public Boolean compareAccessLevel(PlatformUser userToView, PlatformUser requester) {
        Role requesterRole = requester.getRole();
        Role targetRole = userToView.getRole();
        if (requesterRole.equals(Role.ADMIN)) {
            return true;
        } else if (requesterRole.equals(Role.MODERATOR) && !targetRole.equals(Role.ADMIN)) {
            return true;
        } else if ((requesterRole.equals(Role.STUDENT) || requester.getRole().equals(Role.TEACHER)) && !(targetRole.equals(Role.MODERATOR) || targetRole.equals(Role.ADMIN))) {
            return true;
        }
        return false;
    }

    @Override
    public void changePrivateInfo(PlatformUser user, PlatformUserProfile newProfile) {
        PlatformUser userFromDb = userRepository.findByUsername(user.getUsername()).orElseThrow(ResourceNotFoundException::new);
        PlatformUserProfile userProfile = user.getProfile();
        userProfile.setName(newProfile.getName());
        userProfile.setSurname(newProfile.getSurname());
        userProfile.setMiddleName(newProfile.getMiddleName());
        userProfile.setSelfDescription(newProfile.getSelfDescription());
        userProfileRepository.save(userProfile);
        userRepository.save(user);
    }


}
