package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.app.DialogFragment;
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
import ru.geekbrains.lessions2345.notepadonfragments.model.CONSTANTS;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class EditCardFragment extends DialogFragment implements OnClickListener {

    private final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    private DatePicker datePicker;
    private EditText editText_name;
    private EditText editText_description;
    private int activeIndex = 0;
    private Button buttonOk;
    private TextView textView_title;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_card, null);
        initView(view);
        return view;
    }

    private void onCancel(View view) {
        dismiss();
    }

    public void onOk(View v) {
        if (activeIndex <= 0) {
            ((MainActivity) getActivity()).getCardSourceImplement().addCardNote(new CardNote(String.valueOf(editText_name.getText()), String.valueOf(editText_description.getText()), "", this.datePicker.getYear(), this.datePicker.getMonth() + 1, this.datePicker.getDayOfMonth()));
        } else {
            ((MainActivity) getActivity()).getCardSourceImplement().setCardNote(activeIndex, String.valueOf(editText_name.getText()), String.valueOf(editText_description.getText()));
            ((MainActivity) getActivity()).getCardSourceImplement().setCardNote(activeIndex, this.datePicker.getYear(), this.datePicker.getMonth() + 1, this.datePicker.getDayOfMonth());
        }
        // Перезапуск фрагмента со списком для отображения новой заметки
        ((MainActivity) getActivity())
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, ListNotesFragment.newInstance())
                .commit();
        dismiss();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initView(View view) {
        activeIndex = ((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex();

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
            editText_name.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(activeIndex).getName());
            editText_description.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(activeIndex).getDescription());
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
            year = ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(activeIndex).getDateYear();
            month = ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(activeIndex).getDateMonth() - 1;
            day = ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(activeIndex).getDateDay();
        }
//        datePicker.updateDate(year, month, day);
        datePicker.init(year, month, day, null);
    }
}