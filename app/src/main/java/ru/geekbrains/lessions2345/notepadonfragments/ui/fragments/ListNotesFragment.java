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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class ListNotesFragment extends Fragment implements Constants {

    private int newYear;
    private int newMonth;
    private int newDay;

    private Calendar calendar = Calendar.getInstance();
    private final String KEY_INDES_CHOISED_ELEMENT = "ChoisedElement";
    private int indexChoisedElement = 1;
//    private LinearLayout linearLayout = null;
    private CardView cardView = null;

    public static ListNotesFragment newInstance() {
        ListNotesFragment listNotesFragment = new ListNotesFragment();
        return listNotesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Восстановление класса notepad в текущем фрагменте
        if (savedInstanceState == null) {
        } else {
            indexChoisedElement = savedInstanceState.getInt(KEY_INDES_CHOISED_ELEMENT);
        }

//        View view = inflater.inflate(R.layout.fragment_list, container, false);
        View view = inflater.inflate(R.layout.fragment_list_card, container, false);
//        linearLayout = (LinearLayout) view;
        cardView = (CardView) view;

        // Установка значения текстового поля
//        for (int i = 0; i <= notepad.getNumberElements(); i++) {
        int iEndNubmer = ((MainActivity) getActivity()).getCardSourceImplement().size();
        for (int i = 0; i <= iEndNubmer; i++) {
            int sendedIndex = i;
            // Отображение названия заметки
            TextView textView_Name = new TextView(getContext());
            if (i > 0) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textView_Name.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(i).getDescription());
                } else {
                    textView_Name.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(i).getName());
                }
            } else {
                textView_Name.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(i).getName() + "\n");
            }
            // Форматирование текстового поля
            textView_Name.setTextSize(LIST_NAMES_SIZE);
//            linearLayout.addView(textView_Name);
            cardView.addView(textView_Name);
            if (i == 0) {
                textView_Name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Добавление новой заметки
                        ((MainActivity) getActivity()).getCardSourceImplement().addCardNote();

                        // Перезапуск фрагмента со списком для отображения новой заметки
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.list_container, ListNotesFragment.newInstance())
                                .commit();
                        // Загрузка фрагмента c текстом TextFragment
                        indexChoisedElement = 1;
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.text_container, TextFragment.newInstance(indexChoisedElement, true))
                                .commit();
                    }
                });
            } else {
                textView_Name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        indexChoisedElement = sendedIndex;
                        // Загрузка фрагмента c текстом TextFragment
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.text_container, TextFragment.newInstance(indexChoisedElement, false))
                                .commit();
                    }
                });
            }

            // Отображение даты заметки
            TextView textView_Date = new TextView(getContext());
            if (i > 0) {
                textView_Date.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(i).getDate());
                // Форматирование текстового поля
                textView_Date.setTextSize(LIST_DATES_SIZE);
//                linearLayout.addView(textView_Date);
                cardView.addView(textView_Date);
                textView_Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Показать DatePicker для изменения даты заметки
                        showDatePicker(sendedIndex, textView_Date);
                    }
                });
            } else {
                textView_Date.setText("\n");
            }
        }
        return view;
    }

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
                ((MainActivity) getActivity()).getCardSourceImplement().setCardNote(sendedIndex, newYear, newMonth, newDay);
                textView.setText(((MainActivity) getActivity()).getCardSourceImplement().getCardNote(sendedIndex).getDate());
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
        outState.putInt(KEY_INDES_CHOISED_ELEMENT, indexChoisedElement);
        super.onSaveInstanceState(outState);
    }
}
