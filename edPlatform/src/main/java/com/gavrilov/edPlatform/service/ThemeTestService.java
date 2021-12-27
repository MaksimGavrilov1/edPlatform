package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.CourseTest;

public interface ThemeTestService {

    CourseTest save(CourseTest themeTest);

    CourseTest initSave(CourseTest themeTest);
}
