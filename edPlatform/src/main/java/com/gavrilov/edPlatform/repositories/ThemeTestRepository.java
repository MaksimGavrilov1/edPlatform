package com.gavrilov.edPlatform.repositories;

import com.gavrilov.edPlatform.models.ThemeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeTestRepository extends JpaRepository<ThemeTest, Long> {
}