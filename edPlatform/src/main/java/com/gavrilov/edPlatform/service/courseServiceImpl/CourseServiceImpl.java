package com.gavrilov.edPlatform.service.courseServiceImpl;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import com.gavrilov.edPlatform.repo.CourseRepository;
import com.gavrilov.edPlatform.repo.UserRepository;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final int COURSES_TO_SHOW_AMOUNT = 10;

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course createCourse(Course course, Long id) {

        PlatformUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("No user with this id found");
        }
        course.setAuthor(user);
//        user.addOwnedCourse(course);
        //userRepository.save(user);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findCourse(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public List<Course> findCoursesByAuthor(PlatformUser user) {
        return courseRepository.findCourseByAuthor(user);
    }

    @Override
    public List<Course> findCoursesAwaitingConfirmation() {
        return courseRepository.findByStatus(CourseStatus.AWAITING_CONFIRMATION);
    }

    @Override
    public List<Course> findCoursesWithEmptyTestByAuthor(PlatformUser user) {
        return courseRepository.findCourseByAuthor(user).stream()
                .filter(x -> x.getTest() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findTenMostPopular() {
        List<Course> courses = courseRepository.findAll();
        int size = courses.size();
        PriorityQueue<Course> res = new PriorityQueue<>(Comparator.comparingInt((c)->c.getSubscriptions().size()));
        res.addAll(courses);
        List<Course> result = new ArrayList<>();
        for (Course course : courses){
            result.add(res.poll());
        }
        Collections.reverse(result);
        if (size < COURSES_TO_SHOW_AMOUNT){
            return result;
        } else {
            return result.subList(0, COURSES_TO_SHOW_AMOUNT);
        }
    }

    @Override
    public List<Course> findTenNewest() {
        List<Course> courses = courseRepository.findByOrderByIdDesc();
        int size = courses.size();
        if (size < COURSES_TO_SHOW_AMOUNT){
            return courses;
        } else {
            return courses.subList(0, COURSES_TO_SHOW_AMOUNT);
        }
    }

    @Override
    public List<Course> findByPartName(String partName) {
        List<Course> allCourses = courseRepository.findAll();
        String name = partName.toLowerCase(Locale.ROOT).trim();
        return allCourses.stream()
                .filter(x->x.getName().toLowerCase(Locale.ROOT).contains(name))
                .collect(Collectors.toList());
    }
}
