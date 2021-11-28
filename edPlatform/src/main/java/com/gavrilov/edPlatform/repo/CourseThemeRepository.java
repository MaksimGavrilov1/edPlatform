package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.CourseTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseThemeRepository extends JpaRepository<CourseTheme, Long> {
}