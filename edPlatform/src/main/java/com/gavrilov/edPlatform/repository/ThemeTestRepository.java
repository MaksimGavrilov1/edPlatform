package com.gavrilov.edPlatform.repository;

import com.gavrilov.edPlatform.model.ThemeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeTestRepository extends JpaRepository<ThemeTest, Long> {
}