package ru.geekbrains.lessions2345.notepadonfragments.model;

public interface Constants {
    public static final int LIST_NAMES_SIZE = 23;
    public static final int LIST_DATES_SIZE = 17;
//    public static final String KEY_NOTEPAD = "Notepad";
    public static final String KEY_CARD_SOURCE_IMPLEMENT = "CardSourceImplement";
    public static final String KEY_INDEX = "Index";
    public static final String KEY_CREATED_NEW_NOTE = "CreatedNewNote";
    public static final String KEY_DATA_SETTINGS = "Settings";
    public static final int TEST_DATA_INT = 0;
    public static final int FILE_DATA_INT = 1;
    public static final int FIREBASE_DATA_INT = 2;
    public static final int DATABASE_DATA_INT = 3;

    // Settings
    public enum DATA_SETTINGS {
                    // Тестовые данные (значение в SharedPreferences (int) 0)
                    TEST_DATA,
                    // Данные, сохранённые в файле на мобильном устройстве (значение в SharedPreferences (int) 1)
                    FILE_DATA,
                    // Данные, сохранённые в облачной базе данных Firebase (значение в SharedPreferences (int) 2)
                    FIREBASE_DATA,
                    // Данные, сохранённые в локальной базе данных (значение в SharedPreferences (int) 3)
                    DATABASE_DATA
    };
}
