package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionStandardAnswerRepository extends JpaRepository<QuestionStandardAnswer, Long> {
    long deleteByTestQuestion_Id(Long id);
}