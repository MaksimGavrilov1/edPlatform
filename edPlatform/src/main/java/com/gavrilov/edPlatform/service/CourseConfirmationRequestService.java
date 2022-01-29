package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseConfirmationRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;

import java.util.List;

public interface CourseConfirmationRequestService {

    List<CourseConfirmationRequest> findByUser(PlatformUser user);

    List<CourseConfirmationRequest> findAll();

    CourseConfirmationRequest findByCourseAndStatus(Course course, RequestStatus status);

    CourseConfirmationRequest save ( CourseConfirmationRequest request);
}
