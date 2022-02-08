package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.TestQuestion;

import java.util.List;

public interface TestQuestionService {

    TestQuestion save(TestQuestion question);

    List<TestQuestion> findByTest(CourseTest test);
}
