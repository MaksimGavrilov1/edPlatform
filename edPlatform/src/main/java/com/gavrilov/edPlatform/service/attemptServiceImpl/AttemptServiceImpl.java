package com.gavrilov.edPlatform.service.attemptServiceImpl;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.repo.AttemptRepository;
import com.gavrilov.edPlatform.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return attemptRepository.findByCourseTestAndUser(test, user);
    }

    @Override
    public List<Attempt> findLastTenAttempts(PlatformUser user) {
        List<Attempt> attempts = attemptRepository.findByUserOrderByTimeDesc(user);
        int size = attempts.size();
        if (size < ATTEMPTS_TO_SHOW_AMOUNT){
            return attempts;
        } else {
            return attempts.subList(0, ATTEMPTS_TO_SHOW_AMOUNT);
        }
    }

    @Override
    public Attempt findLastAttemptByUserAndTest(PlatformUser user, CourseTest test) {
        return attemptRepository.findFirstByUserAndCourseTestOrderByTimeDesc(user,test);
    }
}
