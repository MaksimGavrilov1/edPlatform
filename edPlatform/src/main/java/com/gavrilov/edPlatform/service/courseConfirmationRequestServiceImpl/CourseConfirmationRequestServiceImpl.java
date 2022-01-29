package com.gavrilov.edPlatform.service.courseConfirmationRequestServiceImpl;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseConfirmationRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import com.gavrilov.edPlatform.repo.CourseConfirmationRequestRepository;
import com.gavrilov.edPlatform.service.CourseConfirmationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseConfirmationRequestServiceImpl implements CourseConfirmationRequestService {

    private final CourseConfirmationRequestRepository courseConfirmationRequestRepository;

    @Override
    public List<CourseConfirmationRequest> findByUser(PlatformUser user) {
        return courseConfirmationRequestRepository.findByUser(user).orElseGet(Collections::emptyList);
    }

    @Override
    public List<CourseConfirmationRequest> findAll() {
        return courseConfirmationRequestRepository.findAll();
    }

    @Override
    public CourseConfirmationRequest findByCourseAndStatus(Course course, RequestStatus status) {
        return courseConfirmationRequestRepository.findByCourseAndStatus(course, status);
    }

    @Override
    public CourseConfirmationRequest save(CourseConfirmationRequest request) {
        return courseConfirmationRequestRepository.save(request);
    }
}
