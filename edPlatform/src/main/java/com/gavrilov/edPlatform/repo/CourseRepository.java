package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Tag;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTagsContaining(Tag tag);

    List<Course> findCourseByAuthor(PlatformUser user);

    List<Course> findByStatus(CourseStatus status);

    List<Course> findByOrderByIdDesc();

    Optional<List<Course>> findCourseByNameContainingIgnoreCase(String name);

    @Query( value = "select course.* from course join tags_courses on course.id = tags_courses.course_id join tag on tag.id = tags_courses.tag_id where tag.name = :name ",
    nativeQuery = true)
    Optional<List<Course>> findCoursesByTagName(@Param("name")String tagName);
    //Optional<List<Course>> findFirst10ById();
}