package ru.geekbrains.lessions2345.notepadonfragments.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity {

    public static int LIST_NAMES_SIZE = 23;
    public static int LIST_DATES_SIZE = 17;
    public static String KEY_NOTEPAD = "Notepad";
    public static String KEY_INDEX = "Index";
    private Notepad notepad = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация класса notepad
        InitNotepad();

        // Отображение фрагмента со списком заметок
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.list_container, ListNotesFragment.newInstance(notepad))
                    .commit();
        }
    }

    private void InitNotepad() {
        // Инициализация класса Notepad
        notepad = new Notepad();
        notepad.add("ПЕРВ.ЗАМ.", "Первая заметка");
        notepad.add("ВТОР.ЗАМ.", "Вторая заметка");
        notepad.add("ТРЕТ.ЗАМ.", "Третья заметка");
        notepad.add("ЧЕТВ.ЗАМ.", "Четвёртая заметка");
        notepad.add("ПЯТ.ЗАМ.", "Пятая заметка");
        notepad.setText(5, "Текст первой заметки");
        notepad.setText(4, "Текст второй заметки");
        notepad.setText(3, "Текст третьей заметки");
        notepad.setText(2, "Текст четвёртой заметки");
        notepad.setText(1, "Текст пятой заметки");
    }

    // Метод для считывания класса во фрагменты
    public Notepad getNotepad() {
        return notepad;
    }

    // Метод для изменения класса notepad через фрагменты
    public void setNotepad(Notepad notepad) {
        this.notepad = notepad;
    }
}