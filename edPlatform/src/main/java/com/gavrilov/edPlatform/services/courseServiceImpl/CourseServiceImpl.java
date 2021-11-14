package com.gavrilov.edPlatform.services.courseServiceImpl;

import com.gavrilov.edPlatform.models.Course;
import com.gavrilov.edPlatform.models.PlatformUser;
import com.gavrilov.edPlatform.repositories.CourseRepository;
import com.gavrilov.edPlatform.repositories.UserRepository;
import com.gavrilov.edPlatform.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public Course createCourse(Course course, Long id) {

        PlatformUser user = userRepository.findById(id).orElse(null);
        if (user == null){
          throw new IllegalArgumentException("No user with this id found");
          //usernotfoundexception свой except и handler
        }
        course.setAuthor(user);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }
}
