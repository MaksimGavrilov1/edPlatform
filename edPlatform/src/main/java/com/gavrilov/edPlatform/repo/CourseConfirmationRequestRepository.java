package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseConfirmationRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseConfirmationRequestRepository extends JpaRepository<CourseConfirmationRequest, Long> {

    Optional<List<CourseConfirmationRequest>> findByUser(PlatformUser user);

    CourseConfirmationRequest findByCourseAndStatus(Course course, RequestStatus status);



}