package ru.geekbrains.lessions2345.notepadonfragments.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity {

    public static String KEY_NOTEPAD = "Notepad";
    Notepad notepad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация класса Notepad
        notepad = new Notepad();
        notepad.add("Первая", "Первый элемент");
        notepad.add("Вторая", "Второй элемент");
        notepad.add("Третья", "Третий элемент");
        notepad.add("Четвёртая", "Четвёртый элемент");
        notepad.add("Пятая", "Пятый элемент");

        Toast.makeText(this, notepad.getName(1), Toast.LENGTH_SHORT).show();

/*
        // добавляем фрагмент
        ListNotesFragment listNotesFragment = new ListNotesFragment();
        // Передача класса notepad во фрагмент ListNotesFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_NOTEPAD, notepad);
        listNotesFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, listNotesFragment)
                .commit();
*/
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, ListNotesFragment.newInstance(notepad))
                .commit();
    }

    // Метод для считывания класса во фрагменты
    public Notepad getNotepad()
    {
        return notepad;
    }

    // Метод для изменения класса notepad через фрагменты
    public void setNotepad(Notepad notepad)
    {
        this.notepad = notepad;
    }
}