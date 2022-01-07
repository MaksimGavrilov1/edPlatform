package com.gavrilov.edPlatform.service.questionStandardAnswerServiceImpl;

import com.gavrilov.edPlatform.model.QuestionStandardAnswer;
import com.gavrilov.edPlatform.repo.QuestionStandardAnswerRepository;
import com.gavrilov.edPlatform.service.QuestionStandardAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionStandardAnswerServiceImpl implements QuestionStandardAnswerService {

    private final QuestionStandardAnswerRepository answerRepository;

    @Override
    public QuestionStandardAnswer save(QuestionStandardAnswer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public long deleteByQuestionId(Long id) {
        return answerRepository.deleteByTestQuestion_Id(id);
    }
}
