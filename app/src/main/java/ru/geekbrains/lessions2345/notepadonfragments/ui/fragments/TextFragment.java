package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class TextFragment extends Fragment implements Constants {

    private int index = 0;
    private EditText editText = null;
    private boolean isCreatedNewNote = false;
    private int numberPreviousElementsInNotepad = 1;

    public static TextFragment newInstance(int index, boolean isCreatedNewNote) {
        TextFragment textFragment = new TextFragment();
        Bundle bundle = new Bundle();
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
        if (savedInstanceState == null) {
            index = getArguments().getInt(KEY_INDEX);
            isCreatedNewNote = getArguments().getBoolean(KEY_CREATED_NEW_NOTE);
            if (isCreatedNewNote == true) {
                ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(1).setText("");
            }
        } else {
            index = savedInstanceState.getInt(KEY_INDEX);
            isCreatedNewNote = false;
        }
        numberPreviousElementsInNotepad = ((MainActivity) getActivity()).getCardSourceImplement().size();

        // Установка значения текстового поля
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        LinearLayout linearLayout = (LinearLayout) view;
        editText = new EditText(getContext());
        editText.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(index).getText());
        // Форматирование текстового поля
        editText.setTextSize(20);
        linearLayout.addView(editText);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (numberPreviousElementsInNotepad != ((MainActivity) getActivity()).getCardSourceImplement().size()) {
            index++;
            isCreatedNewNote = false;
        }
        ((MainActivity) getActivity()).getCardSourceImplement().setCardNote(index, String.valueOf(editText.getText()));
        outState.putInt(KEY_INDEX, index);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        if (numberPreviousElementsInNotepad == ((MainActivity) getActivity()).getCardSourceImplement().size()) {
            ((MainActivity) getActivity()).getCardSourceImplement().setCardNote(index, String.valueOf(editText.getText()));
        } else {
            ((MainActivity) getActivity()).getCardSourceImplement().setCardNote(index + 1, String.valueOf(editText.getText()));
        }
        super.onStop();
    }
}
