package com.gavrilov.edPlatform.service.userAnswerServiceImpl;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.UserAnswer;
import com.gavrilov.edPlatform.repo.UserAnswerRepository;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;
    private final CourseService courseService;

    @Override
    public UserAnswer save(UserAnswer answer) {
        return userAnswerRepository.save(answer);
    }

    @Override
    public List<UserAnswer> findAnswersByUser(PlatformUser user) {
        return userAnswerRepository.findByUser(user);
    }

    @Override
    public List<UserAnswer> findByUserAndCourse(PlatformUser user, Long courseId) {

        List<UserAnswer> answers = userAnswerRepository.findByUser(user);

        return answers.stream()
                .filter(x-> x.getQuestion().getCourseTest().getCourse().getId().equals(courseId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAnswer> findByAttempt(Attempt attempt) {
        return userAnswerRepository.findByAttempt(attempt);
    }
}
