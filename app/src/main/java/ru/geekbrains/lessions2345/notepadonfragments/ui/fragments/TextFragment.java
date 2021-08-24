package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.CardNote;
import ru.geekbrains.lessions2345.notepadonfragments.logic.ListNotes;
import ru.geekbrains.lessions2345.notepadonfragments.model.CONSTANTS;
import ru.geekbrains.lessions2345.notepadonfragments.observer.Observer;
import ru.geekbrains.lessions2345.notepadonfragments.observer.Publisher;
import ru.geekbrains.lessions2345.notepadonfragments.observer.PublisherGetter;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class TextFragment extends Fragment implements Observer {

    private int index = 0;
    private EditText editText = null;
    private boolean isCreatedNewNote = false;
    private int numberPreviousElementsInNotepad = 1;

    private Publisher publisher = null;
    private CardNote cardNote;
    private int activeNoteIndex = 0;
    private boolean deleteMode = false;
    private int oldActiveNoteIndexBeforeDelete = 0;
    private boolean createNewNoteMode = false;

    public static TextFragment newInstance(int index, boolean isCreatedNewNote) {
        TextFragment textFragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CONSTANTS.KEY_INDEX, index);
        bundle.putBoolean(CONSTANTS.KEY_CREATED_NEW_NOTE, isCreatedNewNote);
        textFragment.setArguments(bundle);
        return textFragment;
    }

    // Получение паблишера и подписание на него
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        publisher = ((PublisherGetter) context).getPublisher();
        publisher.subscribe(this);
    }

    // Отписание от паблишера
    @Override
    public void onDetach() {
        super.onDetach();
        publisher.unsubscribe(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*        MainActivity mainActivity = ((MainActivity) getActivity());
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
        linearLayout.addView(editText);*/

        // Установка начальных значений для заметки на фрагменте
        publisher.notify(null, false, -1, false, -1, this.getClass().getSimpleName());
        // Установка значения текстового поля
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        LinearLayout linearLayout = (LinearLayout) view;
        editText = new EditText(getContext());
        editText.setText(cardNote.getText());
        // Форматирование текстового поля
        editText.setTextSize(20);
        linearLayout.addView(editText);
        Toast.makeText(getContext(), String.valueOf(activeNoteIndex  + " " + deleteMode + " " + oldActiveNoteIndexBeforeDelete + " " + createNewNoteMode + " \n" + cardNote.getText()), Toast.LENGTH_SHORT).show();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
/*
        MainActivity mainActivity = ((MainActivity) getActivity());
        if (numberPreviousElementsInNotepad != mainActivity.getCardSourceImplement().size()) {
            index++;
            isCreatedNewNote = false;
        }
        mainActivity.getCardSourceImplement().setCardNote(index, String.valueOf(editText.getText()));
        outState.putInt(CONSTANTS.KEY_INDEX, index);
*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
/*        MainActivity mainActivity = ((MainActivity) getActivity());
        if (numberPreviousElementsInNotepad == mainActivity.getCardSourceImplement().size()) {
            // Случай редактирования элемента
            mainActivity.getCardSourceImplement().setCardNote(index, String.valueOf(editText.getText()));
        } else if (numberPreviousElementsInNotepad < mainActivity.getCardSourceImplement().size()) {
            // Случай добавления элемента
            mainActivity.getCardSourceImplement().setCardNote(index + 1, String.valueOf(editText.getText()));
        } else if (numberPreviousElementsInNotepad > mainActivity.getCardSourceImplement().size()) {
            // Случай удаления элемента
            mainActivity.getCardSourceImplement().setCardNote(mainActivity.getCardSourceImplement().getActiveNoteIndex(), String.valueOf(editText.getText()));
        }*/

        // Установка начальных значений для заметки на фрагменте
        publisher.notify(null, false, -1, false, -1, this.getClass().getSimpleName());
//        cardNote.setText(cardNote.getText());
        cardNote.setText(String.valueOf(editText.getText()));
//        publisher.notify(cardNote, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, "");
        int tempActiveNoteIndex = activeNoteIndex;
        publisher.notify(cardNote, false, oldActiveNoteIndexBeforeDelete, false, oldActiveNoteIndexBeforeDelete, "");
        Toast.makeText(getContext(), String.valueOf(activeNoteIndex + "   " + oldActiveNoteIndexBeforeDelete), Toast.LENGTH_SHORT).show();
        activeNoteIndex = tempActiveNoteIndex;
        publisher.notify(null, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, "");
        super.onStop();
    }

    // Метод обновления данных через паблишер
    @Override
    public void updateState(CardNote cardNote, boolean createNewCardNoteMode, int activeNoteIndex, boolean deleteMode, int oldActiveNoteIndexBeforeDelete, String className) {
        this.cardNote = cardNote;
        this.createNewNoteMode = createNewCardNoteMode;
        this.activeNoteIndex = activeNoteIndex;
        this.deleteMode = deleteMode;
        this.oldActiveNoteIndexBeforeDelete = oldActiveNoteIndexBeforeDelete;
    }
}
