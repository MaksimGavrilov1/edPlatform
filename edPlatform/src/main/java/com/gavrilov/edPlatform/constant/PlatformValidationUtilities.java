package com.gavrilov.edPlatform.constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlatformValidationUtilities {

    public static final Integer MIN_USERNAME_SIZE = 8;
    public static final Integer MIN_PASSWORD_SIZE = 8;
    public static final Integer MAX_COURSE_NAME_SIZE = 80;
    public static final Integer MAX_TAG_SIZE = 50;
    public static final Integer MAX_TEST_QUESTION_AMOUNT = 100;
    public static final Integer MIN_TEST_QUESTION_AMOUNT = 2;
    public static final Integer MAX_TEST_ATTEMPT_AMOUNT = 5;
    public static final Integer MIN_TEST_ATTEMPT_AMOUNT = 1;
    public static final Integer MIN_TEST_ACTIVE_TIME = 1;
    public static final Integer MAX_TEST_ACTIVE_TIME = 366;

    public static String ENG_AND_NUMBERS_PATTERN = "^[a-zA-Z0-9]+$";
    public static String RU_AND_ENG_PATTERN = "^[а-яА-ЯёЁa-zA-Z ]+$";

    public static boolean isNotValidString(String string, String pattern) {
        Pattern validPattern = Pattern.compile(pattern);
        Matcher matcher = validPattern.matcher(string.trim());
        return !matcher.matches();
    }
}
