package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.TestQuestion;
import com.gavrilov.edPlatform.model.CourseTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    List<TestQuestion> findByCourseTest(CourseTest themeTest);

}