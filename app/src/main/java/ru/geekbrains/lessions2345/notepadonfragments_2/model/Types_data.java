package ru.geekbrains.lessions2345.notepadonfragments_2.model;

// Settings
public enum Types_data {
    // Тестовые данные (значение в SharedPreferences (int) 0)
    TEST_DATA,
    // Данные, сохранённые в файле на мобильном устройстве (значение в SharedPreferences (int) 1)
    FILE_DATA,
    // Данные, сохранённые в облачной базе данных Firebase (значение в SharedPreferences (int) 2)
    FIREBASE_DATA,
    // Данные, сохранённые в локальной базе данных (значение в SharedPreferences (int) 3)
    DATABASE_DATA
};
