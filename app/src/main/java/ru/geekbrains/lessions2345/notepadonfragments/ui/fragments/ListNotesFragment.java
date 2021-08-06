package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class ListNotesFragment extends Fragment {

    Notepad notepad;
    int newYear;
    int newMonth;
    int newDay;

    Calendar calendar = Calendar.getInstance();

    public static ListNotesFragment newInstance(Notepad notepad) {
        ListNotesFragment listNotesFragment = new ListNotesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.KEY_NOTEPAD, notepad);
        listNotesFragment.setArguments(bundle);
        return listNotesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Восстановление класса notepad в текущем фрагменте
        if (getArguments() != null) {
            this.notepad = getArguments().getParcelable(MainActivity.KEY_NOTEPAD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        LinearLayout linearLayout = (LinearLayout) view;

        // Установка значения текстового поля
        for (int i = 0; i <= notepad.getNumberElements(); i++) {
            int sendedIndex = i;
            // Отображение названия заметки
            TextView textView_Name = new TextView(getContext());
            if (i > 0) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textView_Name.setText(i + "." + notepad.getDescription(i));
                } else {
                    textView_Name.setText(i + "." + notepad.getName(i));
                }
            } else {
                textView_Name.setText(notepad.getName(i) + "\n");
            }
            // Форматирование текстового поля
            textView_Name.setTextSize(23);
            linearLayout.addView(textView_Name);
            textView_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Загрузка фрагмента c текстом TextFragment
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.text_container, TextFragment.newInstance(notepad, sendedIndex))
                            .commit();
                }
            });

            // Отображение даты заметки
            TextView textView_Date = new TextView(getContext());
            if (i > 0) {
                textView_Date.setText(notepad.getDate(i));
                // Форматирование текстового поля
                textView_Date.setTextSize(17);
                linearLayout.addView(textView_Date);
                textView_Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Показать DatePicker для изменения даты заметки
                        showDatePicker(sendedIndex, textView_Date, view);
                    }
                });
            } else {
                textView_Date.setText("\n");
            }
        }
        return view;
    }

    // Показать DatePicker
    private void showDatePicker(int sendedIndex, TextView textView, View v) {
        // Устанавливаем новую дату
        DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newYear = year;
                newMonth = monthOfYear + 1;
                newDay = dayOfMonth;
                notepad.setDateYear(sendedIndex, newYear);
                notepad.setDateMonth(sendedIndex, newMonth);
                notepad.setDateDay(sendedIndex, newDay);
                textView.setText(notepad.getDate(sendedIndex));
            }
        };
        // Отображаем диалоговое окно для выбора даты
        new DatePickerDialog(getContext(), datePickerDialog,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MainActivity.KEY_NOTEPAD, notepad);
        super.onSaveInstanceState(outState);
    }
}
