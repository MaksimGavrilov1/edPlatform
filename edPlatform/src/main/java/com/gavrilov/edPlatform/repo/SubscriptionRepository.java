package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByUserAndCourse(PlatformUser user, Course course);
    Optional<List<Subscription>> findByUser(PlatformUser user);

    Optional<List<Subscription>> findByCourse(Course course);

    long countByUser(PlatformUser user);

}