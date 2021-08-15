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
import ru.geekbrains.lessions2345.notepadonfragments.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class TextFragment extends Fragment implements Constants {

    private Notepad notepad = null;
    private int index = 0;
    private EditText editText = null;
    private boolean isCreatedNewNote = false;
    private int numberPreviousElementsInNotepad = 1;

    public static TextFragment newInstance(Notepad notepad, int index, boolean isCreatedNewNote) {
        TextFragment textFragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_NOTEPAD, notepad);
        bundle.putInt(KEY_INDEX, index);
        bundle.putBoolean(KEY_CREATED_NEW_NOTE, isCreatedNewNote);
        textFragment.setArguments(bundle);
        return textFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Восстановление notepad и index
        if (savedInstanceState == null)
        {
            notepad = getArguments().getParcelable(KEY_NOTEPAD);
            index = getArguments().getInt(KEY_INDEX);
            isCreatedNewNote = getArguments().getBoolean(KEY_CREATED_NEW_NOTE);
            if (isCreatedNewNote == true)
            {
                notepad.setText(1, "");
                numberPreviousElementsInNotepad = notepad.getNumberElements();
            }
        } else {
            notepad = savedInstanceState.getParcelable(KEY_NOTEPAD);
            index = savedInstanceState.getInt(KEY_INDEX);
            isCreatedNewNote = false;
        }

        // Установка значения текстового поля
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        LinearLayout linearLayout = (LinearLayout) view;
        editText = new EditText(getContext());
        editText.setText(notepad.getText(index));
        // Форматирование текстового поля
        editText.setTextSize(20);
        linearLayout.addView(editText);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

/*        MainActivity mainActivity = (MainActivity) getActivity();
        Notepad newNotepad = mainActivity.getNotepad();
        notepad = newNotepad;
        if (newNotepad.getNumberElements() == numberPreviousElementsInNotepad) {
            notepad.setText(index, String.valueOf(editText.getText()));
            Toast.makeText(mainActivity, "onSaveInstanceState БЕЗ увеличения", Toast.LENGTH_SHORT).show();
        } else {
            notepad.setText(++index, String.valueOf(editText.getText()));
            Toast.makeText(mainActivity, "onSaveInstanceState Увеличение на 1", Toast.LENGTH_SHORT).show();
        }

        notepad.setText(index, String.valueOf(editText.getText()));
        outState.putParcelable(KEY_NOTEPAD, notepad);
        outState.putInt(KEY_INDEX, index);
        numberPreviousElementsInNotepad = notepad.getNumberElements();

        // Передача класса notepad в MainActivity
        mainActivity.setNotepad(notepad);*/

        notepad.setText(index, String.valueOf(editText.getText()));
        outState.putParcelable(KEY_NOTEPAD, notepad);
        outState.putInt(KEY_INDEX, index);
        numberPreviousElementsInNotepad = notepad.getNumberElements();

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        MainActivity mainActivity = (MainActivity) getActivity();
        Notepad newNotepad = mainActivity.getNotepad();

        notepad = newNotepad;

        if (newNotepad.getNumberElements() == numberPreviousElementsInNotepad) {
            notepad.setText(index, String.valueOf(editText.getText()));
            Toast.makeText(mainActivity, "onStop БЕЗ увеличения", Toast.LENGTH_SHORT).show();
        } else {
            notepad.setText(index + 1, String.valueOf(editText.getText()));
            Toast.makeText(mainActivity, "onStop Увеличение на 1", Toast.LENGTH_SHORT).show();
        }
        // Передача класса notepad в MainActivity
        mainActivity.setNotepad(notepad);

        super.onStop();
    }
}
