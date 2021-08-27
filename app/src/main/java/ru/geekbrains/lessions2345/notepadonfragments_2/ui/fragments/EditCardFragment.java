package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments;

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

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.TimeZone;

import ru.geekbrains.lessions2345.notepadonfragments_2.R;
import ru.geekbrains.lessions2345.notepadonfragments_2.logic.CardNote;
import ru.geekbrains.lessions2345.notepadonfragments_2.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.MainActivity;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.Navigation;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.NavigationGetter;

public class EditCardFragment extends DialogFragment implements OnClickListener {

    private final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    private DatePicker datePicker;
    private EditText editText_name;
    private EditText editText_description;
    private int activeIndex = 0;
    private Button buttonOk;
    private TextView textView_title;
    private Navigation navigation;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Получаем навигатор
        navigation = ((NavigationGetter) context).getNavigation();
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
            buttonOk.setText(Constants.NAME_EMPTY_NOTE_ADD);
            textView_title.setText("Карточка новой заметки");
            editText_name.setText(Constants.NAME_EMPTY_NOTE);
            editText_description.setText(Constants.DESCRIPTION_EMPTY_NOTE);
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
        navigation.addFragment(ListNotesFragment.newInstance(), R.id.list_container, false);
/*        mainActivity
            .getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.list_container, ListNotesFragment.newInstance())
            .commit();*/
        dismiss();
    }
}