package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    Optional<List<TestQuestion>> findByCourseTest(CourseTest themeTest);

    long deleteByCourseTest_Id(Long id);

}