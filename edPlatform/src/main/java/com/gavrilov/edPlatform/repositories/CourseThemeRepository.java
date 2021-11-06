package com.gavrilov.edPlatform.repositories;

import com.gavrilov.edPlatform.models.CourseTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseThemeRepository extends JpaRepository<CourseTheme, Long> {
}