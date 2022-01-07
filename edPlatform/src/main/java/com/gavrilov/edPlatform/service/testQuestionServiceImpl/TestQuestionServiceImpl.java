package com.gavrilov.edPlatform.service.testQuestionServiceImpl;

import com.gavrilov.edPlatform.model.TestQuestion;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.repo.TestQuestionRepository;
import com.gavrilov.edPlatform.service.TestQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestQuestionServiceImpl implements TestQuestionService {

    private final TestQuestionRepository testQuestionRepository;

    @Override
    public TestQuestion save(TestQuestion question) {
        return testQuestionRepository.save(question);
    }

    @Override
    public List<TestQuestion> findByTest(CourseTest test) {
        return testQuestionRepository.findByCourseTest(test);
    }

    @Override
    public long deleteByCourseId(Long id) {
        return testQuestionRepository.deleteByCourseTest_Id(id);
    }
}
