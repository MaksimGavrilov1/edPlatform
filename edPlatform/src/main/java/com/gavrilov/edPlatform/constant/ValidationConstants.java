package com.gavrilov.edPlatform.constant;

public class ValidationConstants {

    public static String NOT_EMPTY_USERNAME = "Необходимо указать логин";
    public static String NOT_EMPTY_USERNAME_PASSWORD = "Необходимо ввести пароль";
    public static String NOT_EMPTY_USERNAME_PASSWORD_CONFIRM = "Необходимо повторно ввести пароль";
    public static String NOT_EMPTY_USER_NAME = "Необходимо указать имя";
    public static String NOT_EMPTY_USER_SURNAME = "Необходимо указать фамилию";
    public static String NOT_EMPTY_COURSE_NAME = "Необходмо указать имя курса";
    public static String NOT_EMPTY_COURSE_DESCRIPTION = "Необходмо указать описание курса";
    public static String NOT_EMPTY_COURSE_THEME_NAME = "Необходмо указать имя темы";
    public static String NOT_EMPTY_THEME_TEST_NAME = "Необходмо указать название теста";

    public static String DUPLICATE_AUTHOR_COURSE = "Ранее вами уже был создан курс с таким именем";
    public static String DUPLICATE_USERNAME = "Данный логин уже существует";
    public static String DUPLICATE_COURSE_THEME = "Данная тема уже существует у этого курса";
    public static String LENGTH_USERNAME = "Логин должен быть больше 8 символов";

    public static String FORBIDDEN_SYMBOL_USERNAME = "Логин может включать в себя только латинские буквы и цифры без пробелов";
    public static String FORBIDDEN_SYMBOL_USER_INITIALS = "Только латинские и кириллические буквы";

    public static String USER_PASSWORD_MISMATCH = "Пароли должны совпадать";

    public static String ENG_AND_NUMBERS_PATTERN = "^[a-zA-Z0-9]+$";
    public static String RU_AND_ENG_PATTERN = "^[а-яА-ЯёЁa-zA-Z ]+$";

}
