package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class ListNotesFragment extends Fragment implements Constants, ListNotesFragmentOnClickListener {

    private int newYear;
    private int newMonth;
    private int newDay;

    private Calendar calendar = Calendar.getInstance();
    private final String KEY_INDES_CHOISED_ELEMENT = "ChoisedElement";
    private int indexChoisedElement = 1;
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
        // Восстановление индекса выбранного элемента
        if (savedInstanceState != null) {
            indexChoisedElement = savedInstanceState.getInt(KEY_INDES_CHOISED_ELEMENT);
        }

        // Установка списка заметок к отображению и реагированию на события
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listNotesSetup(view);

        return view;
    }

    // Метод для установки списка заметок к отображению и реагированию на события
    private void listNotesSetup(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_list_container);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        ListNotesAdapter listNotesAdapter = new ListNotesAdapter(((MainActivity) getActivity()).getCardSourceImplement().getListNotes(), getResources().getConfiguration().orientation);

        // Вешаем обработчики событий при нажатии на имя заметки
        listNotesAdapter.setOnListNotesFragmentOnClickListener_name(new ListNotesFragmentOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
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
                } else {
                    // Загрузка фрагмента c текстом TextFragment
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.text_container, TextFragment.newInstance(position, false))
                            .commit();
                }
            }
        });

        // Вешаем обработчики событий при нажатии на дату заметки
        listNotesAdapter.setOnListNotesFragmentOnClickListener_date(new ListNotesFragmentOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Показать DatePicker для изменения даты заметки
                showDatePicker(position, ((MainActivity) getActivity()), listNotesAdapter);
            }
        });

        recyclerView.setAdapter(listNotesAdapter);
    }

    // Показать DatePicker
    private void showDatePicker(int sendedIndex, MainActivity mainActivity, ListNotesAdapter listNotesAdapter) {
        // Устанавливаем новую дату
        DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newYear = year;
                newMonth = monthOfYear + 1;
                newDay = dayOfMonth;
                mainActivity.getCardSourceImplement().setCardNote(sendedIndex, newYear, newMonth, newDay);
                listNotesAdapter.setListNotesDate(sendedIndex, ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(sendedIndex).getDate());
                listNotesAdapter.notifyItemChanged(sendedIndex);
            }
        };
        // Отображаем диалоговое окно для выбора даты
        new DatePickerDialog(getContext(), datePickerDialog,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // Сохранение промежуточного состояния при повороте экрана
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_INDES_CHOISED_ELEMENT, indexChoisedElement);
        super.onSaveInstanceState(outState);
    }

    // Унаследованный метод от интерфейса ListNotesFragmentOnClickListener. Должен быть, но здесь не используется
    @Override
    public void onClick(View view, int position) {
    }
}