package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    long countByUserAndPass(PlatformUser user, Boolean pass);

    long countByUser(PlatformUser user);
    List<Attempt> findByUserOrderByTimeDesc(PlatformUser user);
    List<Attempt> findByUser(PlatformUser user);

    Optional<List<Attempt>> findByCourseTestAndUser(CourseTest courseTest, PlatformUser user);

    Attempt findFirstByUserAndCourseTestOrderByTimeDesc(PlatformUser user, CourseTest courseTest);


}