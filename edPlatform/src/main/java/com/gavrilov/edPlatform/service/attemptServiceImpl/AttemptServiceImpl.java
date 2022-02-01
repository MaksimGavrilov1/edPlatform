package com.gavrilov.edPlatform.service.attemptServiceImpl;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.repo.AttemptRepository;
import com.gavrilov.edPlatform.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttemptServiceImpl implements AttemptService {

    private final AttemptRepository attemptRepository;
    private final Integer ATTEMPTS_TO_SHOW_AMOUNT = 10;

    @Override
    public Attempt save(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    @Override
    public Attempt findById(Long id) {
        return attemptRepository.findById(id).orElse(null);
    }

    @Override
    public List<Attempt> findByUser(PlatformUser user) {
        return attemptRepository.findByUser(user);
    }

    @Override
    public List<Attempt> findByUserAndTest(PlatformUser user, CourseTest test) {
        return attemptRepository.findByCourseTestAndUser(test, user).orElseGet(Collections::emptyList);
    }

    @Override
    public List<Attempt> findLastTenAttempts(PlatformUser user) {
        List<Attempt> attempts = attemptRepository.findByUserOrderByTimeDesc(user);
        int size = attempts.size();
        if (size < ATTEMPTS_TO_SHOW_AMOUNT) {
            return attempts;
        } else {
            return attempts.subList(0, ATTEMPTS_TO_SHOW_AMOUNT);
        }
    }

    @Override
    public Attempt findLastAttemptByUserAndTest(PlatformUser user, CourseTest test) {
        return attemptRepository.findFirstByUserAndCourseTestOrderByTimeDesc(user, test);
    }

    @Override
    public Long countUserTestResult(PlatformUser user, Boolean passed) {
        return attemptRepository.countByUserAndPass(user, passed);
    }

    @Override
    public Long countUserAttempts(PlatformUser user) {
        return attemptRepository.countByUser(user);
    }

    @Override
    public Boolean anyAttemptsLeft(PlatformUser user, CourseTest test) {
        List<Attempt> userAttempts = attemptRepository.findByCourseTestAndUser(test, user).orElseGet(Collections::emptyList);
        return userAttempts.size() < test.getAmountOfAttempts();
    }

    @Override
    public Attempt initAttempt(PlatformUser user, CourseTest test) {
        Attempt attempt = new Attempt();
        attempt.setUser(user);
        attempt.setTime(new Timestamp(new Date().getTime()));
        attempt.setCourseTest(test);
        attempt.setMark(0);
        attempt.setPass(false);
        return attemptRepository.save(attempt);
    }




}
