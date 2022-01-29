package com.gavrilov.edPlatform.service;


import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Subscription;

import java.util.List;

public interface SubscriptionService {

    Subscription save (Subscription sub);

    List<Subscription> findByUser(PlatformUser user);

    void updateSubscriptionStatus(PlatformUser user);

    void updateSubscriptionStatusByUserAndCourse(PlatformUser user, Course course);

    boolean isUserSubOnCourse(PlatformUser user, Course course);

    Subscription findByUserAndCourse(PlatformUser user, Course course);

    List<Subscription> findByCourse (Course course);

}
