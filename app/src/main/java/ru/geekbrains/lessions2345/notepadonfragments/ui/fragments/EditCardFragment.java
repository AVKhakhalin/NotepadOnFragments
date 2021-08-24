package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.CardNote;
import ru.geekbrains.lessions2345.notepadonfragments.logic.ListNotes;
import ru.geekbrains.lessions2345.notepadonfragments.model.CONSTANTS;
import ru.geekbrains.lessions2345.notepadonfragments.observer.Observer;
import ru.geekbrains.lessions2345.notepadonfragments.observer.Publisher;
import ru.geekbrains.lessions2345.notepadonfragments.observer.PublisherGetter;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class EditCardFragment extends DialogFragment implements OnClickListener, Observer {

    private final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    private DatePicker datePicker = null;
    private EditText editText_name = null;
    private EditText editText_description = null;
    private int activeIndex = 0;
    private Button buttonOk = null;
    private TextView textView_title = null;

    private Publisher publisher = null;
    private CardNote cardNote = null;
    private int activeNoteIndex = 0;
    private boolean deleteMode = false;
    private int oldActiveNoteIndexBeforeDelete = 0;
    private boolean createNewNoteMode = false;

    // Получение паблишера и подписание на него
    @Override
    public void onAttach(Context context) {
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_card, null);
        MainActivity mainActivity = ((MainActivity) getActivity());
        initView(view, mainActivity);
        return view;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initView(View view, MainActivity mainActivity) {
        activeIndex = mainActivity.getCardSourceImplement().getActiveNoteIndex();
        textView_title = view.findViewById(R.id.card_note_title);
        editText_name = view.findViewById(R.id.card_name_note_text);
        editText_description = view.findViewById(R.id.card_description_note_text);
        buttonOk = view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(this::onOk);
        datePicker = view.findViewById(R.id.inputDate);

        if (activeIndex <= 0) {
            buttonOk.setText(CONSTANTS.NAME_EMPTY_NOTE_ADD);
            textView_title.setText("Карточка новой заметки");
            editText_name.setText(CONSTANTS.NAME_EMPTY_NOTE);
            editText_description.setText(CONSTANTS.DESCRIPTION_EMPTY_NOTE);
        } else {
            editText_name.setText(mainActivity.getCardSourceImplement().getCardNote(activeIndex).getName());
            editText_description.setText(mainActivity.getCardSourceImplement().getCardNote(activeIndex).getDescription());
        }
        view.findViewById(R.id.button_cancel).setOnClickListener(this::onCancel);
        int year;
        int month;
        int day;
        if (activeIndex <= 0) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = mainActivity.getCardSourceImplement().getCardNote(activeIndex).getDateYear();
            month = mainActivity.getCardSourceImplement().getCardNote(activeIndex).getDateMonth() - 1;
            day = mainActivity.getCardSourceImplement().getCardNote(activeIndex).getDateDay();
        }
        datePicker.init(year, month, day, null);
    }

    // Результат нажатия на кнопку отмены действия
    private void onCancel(View view) {
        dismiss();
    }

    // Результат нажатия на кнопку подтверждения действия
    public void onOk(View v) {
        MainActivity mainActivity = ((MainActivity) getActivity());
        if (activeIndex <= 0) {
            mainActivity.getCardSourceImplement().addCardNote(new CardNote(String.valueOf(editText_name.getText()), String.valueOf(editText_description.getText()), "", this.datePicker.getYear(), this.datePicker.getMonth() + 1, this.datePicker.getDayOfMonth()));
        } else {
            mainActivity.getCardSourceImplement().setCardNote(activeIndex, String.valueOf(editText_name.getText()), String.valueOf(editText_description.getText()));
            mainActivity.getCardSourceImplement().setCardNote(activeIndex, this.datePicker.getYear(), this.datePicker.getMonth() + 1, this.datePicker.getDayOfMonth());
        }
        // Перезапуск фрагмента со списком для отображения новой заметки
        mainActivity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, ListNotesFragment.newInstance())
                .commit();
        dismiss();
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