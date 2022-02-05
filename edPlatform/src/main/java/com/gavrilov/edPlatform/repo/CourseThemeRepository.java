package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseThemeRepository extends JpaRepository<CourseTheme, Long> {
    Optional< List<CourseTheme>> findByCourse(Course course);
}