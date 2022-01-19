package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.dto.TestDto;
import com.gavrilov.edPlatform.dto.TestResultDto;
import com.gavrilov.edPlatform.model.CourseTest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.UserAnswer;

import java.util.List;

public interface ThemeTestService {

//    CourseTest save(CourseTest themeTest);

    CourseTest initSave(CourseTest themeTest);

    CourseTest findTest (Long id);

    CourseTest randomizeAnswers(CourseTest test);

    TestResultDto calculateResult(TestDto source, PlatformUser user);

    TestResultDto formResult(List<UserAnswer> ansers, Long courseId);
}