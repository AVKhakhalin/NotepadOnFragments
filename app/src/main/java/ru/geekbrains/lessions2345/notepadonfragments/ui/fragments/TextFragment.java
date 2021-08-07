package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class TextFragment extends Fragment {

    private Notepad notepad = null;
    private int index = 0;
    private EditText editText = null;
    private boolean isCreatedNewNote = false;
    private int numberPreviousElementsInNotepad = 1;

    public static TextFragment newInstance(Notepad notepad, int index, boolean isCreatedNewNote) {
        TextFragment textFragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.KEY_NOTEPAD, notepad);
        bundle.putInt(MainActivity.KEY_INDEX, index);
        bundle.putBoolean(MainActivity.KEY_CREATED_NEW_NOTE, isCreatedNewNote);
        textFragment.setArguments(bundle);
        return textFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Восстановление notepad и index
        if (getArguments() != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            notepad = mainActivity.getNotepad();
            if (notepad == null)
            {
                notepad = getArguments().getParcelable(MainActivity.KEY_NOTEPAD);
//                Toast.makeText(mainActivity, String.valueOf(notepad.getText(2)), Toast.LENGTH_SHORT).show();
            }
            numberPreviousElementsInNotepad = notepad.getNumberElements();
            index = getArguments().getInt(MainActivity.KEY_INDEX);
//            Toast.makeText(mainActivity, String.valueOf(index), Toast.LENGTH_SHORT).show();
            isCreatedNewNote = getArguments().getBoolean(MainActivity.KEY_CREATED_NEW_NOTE);
            if (isCreatedNewNote == true) {
                notepad.setText(1, "");
            }
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
        MainActivity mainActivity = (MainActivity) getActivity();
        Notepad newNotepad = mainActivity.getNotepad();
        if (newNotepad.getNumberElements() == numberPreviousElementsInNotepad) {
            notepad.setText(index, String.valueOf(editText.getText()));
        } else {
            notepad.setText(index + 1, String.valueOf(editText.getText()));
        }
        // Передача класса notepad в MainActivity
        mainActivity.setNotepad(notepad);
        super.onStop();
    }
}
