package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByCourse(Course course);
}