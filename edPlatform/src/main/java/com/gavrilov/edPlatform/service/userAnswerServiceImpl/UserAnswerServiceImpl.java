package com.gavrilov.edPlatform.service.userAnswerServiceImpl;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.UserAnswer;
import com.gavrilov.edPlatform.repo.UserAnswerRepository;
import com.gavrilov.edPlatform.service.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    @Override
    public UserAnswer save(UserAnswer answer) {
        return userAnswerRepository.save(answer);
    }





    @Override
    public List<UserAnswer> findByAttempt(Attempt attempt) {
        return userAnswerRepository.findByAttempt(attempt).orElseGet(Collections::emptyList);
    }
}
