package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.dto.TestDto;
import com.gavrilov.edPlatform.dto.TestResultDto;
import com.gavrilov.edPlatform.model.CourseTest;

public interface ThemeTestService {

    CourseTest save(CourseTest themeTest);

    CourseTest initSave(CourseTest themeTest);

    CourseTest findTest (Long id);

    TestResultDto calculateResult(TestDto source);
}
