package com.gavrilov.edPlatform.service.courseServiceImpl;

import com.gavrilov.edPlatform.exception.ResourceNotFoundException;
import com.gavrilov.edPlatform.model.*;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import com.gavrilov.edPlatform.model.enumerator.CourseSubscriptionStatus;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import com.gavrilov.edPlatform.model.enumerator.Role;
import com.gavrilov.edPlatform.repo.CourseRepository;
import com.gavrilov.edPlatform.repo.UserRepository;
import com.gavrilov.edPlatform.service.CourseConfirmationRequestService;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.SubscriptionService;
import com.gavrilov.edPlatform.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final CourseConfirmationRequestService courseConfirmationRequestService;
    private final int COURSES_TO_SHOW_AMOUNT = 10;

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Course> findCoursesByAuthor(PlatformUser user) {
        return courseRepository.findCourseByAuthor(user).orElseGet(Collections::emptyList);
    }



    @Override
    public List<Course> findCoursesWithEmptyTestByAuthor(PlatformUser user) {
        return courseRepository.findCourseByAuthor(user).orElseGet(Collections::emptyList).stream()
                .filter(x -> x.getTest() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findTenMostPopular(PlatformUser user) {
        List<Course> courses = courseRepository.findByStatus(CourseStatus.APPROVED).orElseGet(Collections::emptyList);
        courses.sort(Comparator.comparingInt(c->c.getSubscriptions().size()));
        Collections.reverse(courses);
        return cutCourses(courses);
    }

    @Override
    public List<Course> findTenNewest(PlatformUser user) {
        List<Course> courses = courseRepository.findByStatusOrderByIdDesc(CourseStatus.APPROVED).orElseGet(Collections::emptyList);
        return cutCourses(courses);
    }

    private List<Course> cutCourses(List<Course> courses) {
        int size = courses.size();
        if (size < COURSES_TO_SHOW_AMOUNT){
            return courses;
        } else {
            return courses.subList(0, COURSES_TO_SHOW_AMOUNT);
        }
    }

    @Override
    public List<Course> findByPartName(String partName) {
        return courseRepository.findCourseByNameContainingIgnoreCase(partName).orElseGet(Collections::emptyList);
    }

    @Override
    public List<Course> findCoursesByTag(String tag) {
        return courseRepository.findCoursesByTagName(tag).orElseGet(Collections::emptyList);
    }

    @Override
    public List<Course> findByStatus(CourseStatus status) {
        return courseRepository.findByStatus(status).orElseGet(Collections::emptyList);
    }

    @Override
    public List<Course> findByAuthorAndStatus(PlatformUser user, CourseStatus status) {
        return courseRepository.findByAuthorAndStatus(user, status).orElseGet(Collections::emptyList);
    }

    @Override
    public Long countByAuthor(PlatformUser author) {
        return courseRepository.countByAuthor(author);
    }

    @Override
    public boolean isValidToApprove(Course course) {
        return course.getTest() != null && course.getThemes() != null;
    }

    @Override
    public void denyCourse(Course course, String reason) {
        course.setStatus(CourseStatus.DRAFT);
        courseRepository.save(course);
        CourseConfirmationRequest request = courseConfirmationRequestService.findByCourseAndStatus(course, RequestStatus.PENDING);
        request.setStatus(RequestStatus.DECLINED);
        request.setReason(reason);
        request.setAnswerDate(new Timestamp(new Date().getTime()));
        courseConfirmationRequestService.save(request);
    }

    @Override
    public void approveCourse(Course course, PlatformUser user) {
        course.setStatus(CourseStatus.APPROVED);
        courseRepository.save(course);
        CourseConfirmationRequest request = courseConfirmationRequestService.findByCourseAndStatus(course, RequestStatus.PENDING);
        request.setStatus(RequestStatus.APPROVED);
        request.setReason("Курс одобрен");
        request.setAnswerDate(new Timestamp(new Date().getTime()));
        courseConfirmationRequestService.save(request);
        user.setRole(Role.TEACHER);
        userRepository.save(user);
    }

    @Override
    public void submitToApprove(Course course, PlatformUser user) {
        course.setStatus(CourseStatus.AWAITING_CONFIRMATION);
        courseRepository.save(course);
        CourseConfirmationRequest request = new CourseConfirmationRequest();
        request.setCourse(course);
        request.setUser(user);
        request.setSubmitDate(new Timestamp(new Date().getTime()));
        request.setStatus(RequestStatus.PENDING);
        courseConfirmationRequestService.save(request);
    }

    @Override
    public void archiveCourseByCourseId(Long courseId) {
        Course course = changeStatus(courseId, CourseStatus.ARCHIVED);
        List<Subscription> subscriptions = subscriptionService.findByCourse(course);
        for (Subscription sub : subscriptions){
            sub.setStatus(CourseSubscriptionStatus.ARCHIVED);
            sub.setCourseEndDate(new Timestamp(new Date().getTime()));
            subscriptionService.save(sub);
        }
    }

    @Override
    public void unarchiveCourse(Long id) {
        Course course = changeStatus(id, CourseStatus.APPROVED);
        List<Subscription> subscriptions = subscriptionService.findByCourse(course);
        for (Subscription sub : subscriptions){
            sub.setStatus(CourseSubscriptionStatus.OPEN);
            sub.setDateOfSubscription(new Timestamp(new Date().getTime()));
            long endDate = new Date().getTime() + course.getActiveTime().getTime();
            sub.setCourseEndDate(new Timestamp(endDate));
            subscriptionService.save(sub);
        }
    }

    private Course changeStatus(Long courseId, CourseStatus status) {
        Course course = courseRepository.findById(courseId).orElseThrow(ResourceNotFoundException::new);
        course.setStatus(status);
        return courseRepository.save(course);
    }
}
