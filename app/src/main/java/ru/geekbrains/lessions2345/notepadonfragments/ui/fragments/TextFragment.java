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
import ru.geekbrains.lessions2345.notepadonfragments.model.CONSTANTS;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class TextFragment extends Fragment {

    private int index = 0;
    private EditText editText = null;
    private boolean isCreatedNewNote = false;
    private int numberPreviousElementsInNotepad = 1;

    public static TextFragment newInstance(int index, boolean isCreatedNewNote) {
        TextFragment textFragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CONSTANTS.KEY_INDEX, index);
        bundle.putBoolean(CONSTANTS.KEY_CREATED_NEW_NOTE, isCreatedNewNote);
        textFragment.setArguments(bundle);
        return textFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = ((MainActivity) getActivity());
        // Восстановление notepad и index
        if (savedInstanceState == null) {
            index = getArguments().getInt(CONSTANTS.KEY_INDEX);
            isCreatedNewNote = getArguments().getBoolean(CONSTANTS.KEY_CREATED_NEW_NOTE);
            if (isCreatedNewNote == true) {
                mainActivity.getCardSourceImplement().getCardNote(1).setText("");
            }
        } else {
            index = savedInstanceState.getInt(CONSTANTS.KEY_INDEX);
            isCreatedNewNote = false;
        }
        numberPreviousElementsInNotepad = mainActivity.getCardSourceImplement().size();

        // Установка значения текстового поля
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        LinearLayout linearLayout = (LinearLayout) view;
        editText = new EditText(getContext());
        editText.setText(mainActivity.getCardSourceImplement().getCardNote(index).getText());
        // Форматирование текстового поля
        editText.setTextSize(20);
        linearLayout.addView(editText);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        MainActivity mainActivity = ((MainActivity) getActivity());
        if (numberPreviousElementsInNotepad != mainActivity.getCardSourceImplement().size()) {
            index++;
            isCreatedNewNote = false;
        }
        mainActivity.getCardSourceImplement().setCardNote(index, String.valueOf(editText.getText()));
        outState.putInt(CONSTANTS.KEY_INDEX, index);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        MainActivity mainActivity = ((MainActivity) getActivity());
        if (numberPreviousElementsInNotepad == mainActivity.getCardSourceImplement().size()) {
            // Случай редактирования элемента
            mainActivity.getCardSourceImplement().setCardNote(index, String.valueOf(editText.getText()));
        } else if (numberPreviousElementsInNotepad < mainActivity.getCardSourceImplement().size()) {
            // Случай добавления элемента
            mainActivity.getCardSourceImplement().setCardNote(index + 1, String.valueOf(editText.getText()));
        } else if (numberPreviousElementsInNotepad > mainActivity.getCardSourceImplement().size()) {
            // Случай удаления элемента
            mainActivity.getCardSourceImplement().setCardNote(mainActivity.getCardSourceImplement().getActiveNoteIndex(), String.valueOf(editText.getText()));
        }
        super.onStop();
    }
}
