package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.PlatformUser;

import java.util.List;

public interface AttemptService {

    Attempt save(Attempt attempt);

    Attempt findById(Long id);

    List<Attempt> findByUser(PlatformUser user);

    List<Attempt> findByUserAndTest(PlatformUser user, CourseTest test);

    List<Attempt> findLastTenAttempts(PlatformUser user);

    Attempt findLastAttemptByUserAndTest(PlatformUser user, CourseTest test);

    Long countUserTestResult(PlatformUser user, Boolean passed);

    Long countUserAttempts(PlatformUser user);

    Boolean anyAttemptsLeft(PlatformUser user, CourseTest test);

    Attempt initAttempt(PlatformUser user, CourseTest test);

}
