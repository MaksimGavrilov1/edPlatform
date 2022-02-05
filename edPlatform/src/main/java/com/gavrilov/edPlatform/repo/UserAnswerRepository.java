package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    Optional<List<UserAnswer>> findByAttempt(Attempt attempt);
    Optional<List<UserAnswer>> findByUser(PlatformUser user);
}