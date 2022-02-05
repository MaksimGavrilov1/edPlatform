package com.gavrilov.edPlatform.service.SubscriptionServiceImpl;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Subscription;
import com.gavrilov.edPlatform.model.enumerator.CourseSubscriptionStatus;
import com.gavrilov.edPlatform.repo.SubscriptionRepository;
import com.gavrilov.edPlatform.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription save(Subscription sub) {
        return subscriptionRepository.save(sub);
    }

    @Override
    public List<Subscription> findByUser(PlatformUser user) {
        return subscriptionRepository.findByUser(user).orElseGet(Collections::emptyList);
    }

    @Override
    public void updateSubscriptionStatus(PlatformUser user) {
        List<Subscription> userSubs = subscriptionRepository.findByUser(user).orElseGet(Collections::emptyList);
        for (Subscription sub : userSubs){
            updateSub(sub);
        }
    }

    @Override
    public void updateSubscriptionStatusByUserAndCourse(PlatformUser user, Course course) {
        Subscription sub = subscriptionRepository.findByUserAndCourse(user, course);
        updateSub(sub);
    }

    private void updateSub(Subscription sub){
        long timeNow = new Date().getTime();
        if (sub.getCourseEndDate().getTime() - timeNow <= 0 && sub.getCourse().getIsAlwaysOpen() && !sub.getStatus().equals(CourseSubscriptionStatus.ARCHIVED)){
            sub.setStatus(CourseSubscriptionStatus.EXPIRED);
            subscriptionRepository.save(sub);
        } else if (sub.getCourseEndDate().getTime() - timeNow <= 0 && !sub.getCourse().getIsAlwaysOpen() && !sub.getStatus().equals(CourseSubscriptionStatus.ARCHIVED)){
            sub.setStatus(CourseSubscriptionStatus.CLOSED);
            subscriptionRepository.save(sub);
        }
    }

    @Override
    public boolean isUserSubOnCourse(PlatformUser user, Course course) {
        Subscription sub = subscriptionRepository.findByUserAndCourse(user, course);
        return sub != null;
    }

    @Override
    public Subscription findByUserAndCourse(PlatformUser user, Course course) {
        return subscriptionRepository.findByUserAndCourse(user, course);
    }

    @Override
    public List<Subscription> findByCourse(Course course) {
        return subscriptionRepository.findByCourse(course).orElseGet(Collections::emptyList);
    }

    @Override
    public Long countByUser(PlatformUser user) {
        return subscriptionRepository.countByUser(user);
    }

    @Override
    public Subscription subscribeUser(PlatformUser user, Course course) {
        Subscription sub = new Subscription();
        sub.setCourse(course);
        sub.setUser(user);
        sub.setDateOfSubscription(new Timestamp(new Date().getTime()));
        long endOfSub = sub.getDateOfSubscription().getTime() + course.getActiveTime().getTime();
        sub.setCourseEndDate(new Timestamp(endOfSub));
        sub.setStatus(CourseSubscriptionStatus.OPEN);
        return subscriptionRepository.save(sub);
    }
}
