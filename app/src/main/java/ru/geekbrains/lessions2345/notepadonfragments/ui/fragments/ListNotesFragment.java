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

    private Notepad notepad;
    private int newYear;
    private int newMonth;
    private int newDay;

    private Calendar calendar = Calendar.getInstance();
    private final String KEY_INDES_CHOISED_ELEMENT = "ChoisedElement";
    private int indexChoisedElement = 0;

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
        createList(linearLayout);
        return view;
    }

    private void createList(LinearLayout linearLayout) {
        for (int i = 0; i <= notepad.getNumberElements(); i++) {
            // Отображение названия заметки
            linearLayout.addView(createNameTextView(i));
            if (i > 0) {
                linearLayout.addView(createDateTextView(i));
            }
        }
    }

    private View createNameTextView(int sendingIndex) {
        TextView textView_Name = new TextView(getContext());
        // Форматирование текстового поля
        textView_Name.setTextSize(MainActivity.LIST_NAMES_SIZE);
        if (sendingIndex == 0) {
            textView_Name.setText(String.format("%s\n", notepad.getName(sendingIndex)));
            textView_Name.setOnClickListener(newNoteClickListener);
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textView_Name.setText(notepad.getDescription(sendingIndex));
            } else {
                textView_Name.setText(notepad.getName(sendingIndex));
            }
            textView_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexChoisedElement = sendingIndex;
                    // Загрузка фрагмента c текстом TextFragment
                    showTextFragment(sendingIndex, false);
                }
            });
        }
        return textView_Name;
    }

    private View createDateTextView(int sendingIndex) {
        // Отображение даты заметки
        TextView textView_Date = new TextView(getContext());
        // Форматирование текстового поля
        textView_Date.setTextSize(MainActivity.LIST_DATES_SIZE);
        textView_Date.setText(String.format("%s\n", notepad.getDate(sendingIndex)));


        textView_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Показать DatePicker для изменения даты заметки
                showDatePicker(sendingIndex, textView_Date);
            }
        });
        return textView_Date;
    }

    private void showTextFragment(int sendingIndex, boolean b) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.text_container, TextFragment.newInstance(notepad, sendingIndex, b))
                .commit();
    }

    private void showTextListNotesFragment() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, ListNotesFragment.newInstance(notepad))
                .commit();
    }

    private View.OnClickListener newNoteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Добавление новой заметки
            notepad.add("", "");
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setNotepad(notepad);
            // Перезапуск фрагмента со списком для отображения новой заметки
            showTextListNotesFragment();
            // Загрузка фрагмента c текстом TextFragment
            indexChoisedElement = 1;
            showTextFragment(indexChoisedElement, true);
        }
    };


    // Показать DatePicker
    private void showDatePicker(int sendedIndex, TextView textView) {
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
        outState.putInt(KEY_INDES_CHOISED_ELEMENT, indexChoisedElement);
        super.onSaveInstanceState(outState);
    }
}
