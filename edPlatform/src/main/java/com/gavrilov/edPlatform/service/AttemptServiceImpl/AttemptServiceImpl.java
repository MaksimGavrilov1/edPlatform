package com.gavrilov.edPlatform.service.AttemptServiceImpl;

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
}
