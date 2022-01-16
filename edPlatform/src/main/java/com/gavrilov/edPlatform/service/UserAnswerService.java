package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.UserAnswer;

import java.util.List;

public interface UserAnswerService {

    UserAnswer save(UserAnswer answer);

    List<UserAnswer> findAnswersByUser(PlatformUser user);

    List<UserAnswer> findByUserAndCourse(PlatformUser user, Long courseId);

}
