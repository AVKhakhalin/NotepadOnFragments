package ru.geekbrains.lessions2345.notepadonfragments.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity {

    public static String KEY_NOTEPAD = "Notepad";
    public static String KEY_INDEX = "Index";
    Notepad notepad;

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
        notepad.add("Первая", "Первый элемент");
        notepad.add("Вторая", "Второй элемент");
        notepad.add("Третья", "Третий элемент");
        notepad.add("Четвёртая", "Четвёртый элемент");
        notepad.add("Пятая", "Пятый элемент");
        notepad.setText(1, "Текст первого элемента");
        notepad.setText(2, "Текст второго элемента");
        notepad.setText(3, "Текст третьего элемента");
        notepad.setText(4, "Текст четвёртого элемента");
        notepad.setText(5, "Текст пятого элемента");
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