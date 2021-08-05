package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class TextFragment extends Fragment {

    Notepad notepad;
    private int index = 1;
    EditText editText;

    public static TextFragment newInstance(Notepad notepad, int index) {
        TextFragment textFragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.KEY_NOTEPAD, notepad);
        bundle.putInt(MainActivity.KEY_INDEX, index);
        textFragment.setArguments(bundle);
        return textFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Восстановление данных notepad и index
        if (getArguments() != null) {
            notepad = getArguments().getParcelable(MainActivity.KEY_NOTEPAD);
            index = getArguments().getInt(MainActivity.KEY_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        LinearLayout linearLayout = (LinearLayout) view;

        // Установка значения текстового поля
        editText = new EditText(getContext());
        editText.setText(notepad.getText(index));
        // Форматирование текстового поля
        editText.setTextSize(20);
        linearLayout.addView(editText);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        notepad.setText(index, String.valueOf(editText.getText()));
        outState.putParcelable(MainActivity.KEY_NOTEPAD, notepad);
        outState.putInt(MainActivity.KEY_INDEX, index);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        notepad.setText(index, String.valueOf(editText.getText()));
        // Получение класса notepad из MainActivity
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setNotepad(notepad);
        Toast.makeText(mainActivity, "Сохранение заметки", Toast.LENGTH_SHORT).show();
        super.onStop();
    }
}
