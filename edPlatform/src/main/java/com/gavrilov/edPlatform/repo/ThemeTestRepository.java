package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.CourseTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeTestRepository extends JpaRepository<CourseTest, Long> {
    CourseTest findByName(String name);
}