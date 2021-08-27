package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

import ru.geekbrains.lessions2345.notepadonfragments_2.R;
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.Publisher;
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.PublisherGetter;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.MainActivity;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.Navigation;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.NavigationGetter;

public class ListNotesFragment extends Fragment implements ListNotesFragmentOnClickListener {

    private int newYear;
    private int newMonth;
    private int newDay;

    private final String KEY_INDES_CHOISED_ELEMENT = "ChoisedElement";
    private int indexChoisedElement = 1;

    private ListNotesAdapter listNotesAdapter;
    private EditCardFragment editCardFragment;

    private Navigation navigation;
    private Publisher publisher = new Publisher();

    public static ListNotesFragment newInstance() {
        ListNotesFragment listNotesFragment = new ListNotesFragment();
        return listNotesFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Получаем навигатор
        navigation = ((NavigationGetter) context).getNavigation();
        // Получаем паблишер, чтобы передать с ним в MainActivity результат выбора пользователя по удалению заметки
        publisher = ((PublisherGetter) context).getPublisher();
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
        RecyclerView recyclerView = view.findViewById(R.id.recycler_list_container);
        listNotesSetup(recyclerView);

        return view;
    }

    // Метод для установки и отображения списка заметок, а также установки на его элементы обработчики событий
    private void listNotesSetup(RecyclerView recyclerView) {
        MainActivity mainActivity = ((MainActivity) getActivity());
        // Устанавливаем признак одинаковости элементов списка
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // Устанавливаем настройки адаптера listNotesAdapter
        setUpListNotesAdapter(mainActivity);
        recyclerView.setAdapter(listNotesAdapter);

        // Восстановление отображения содержимого просматриваемой заметки до удаления другой заметки через контекстное меню
        int oldActiveNoteIndexBeforeDelete = mainActivity.getCardSourceImplement().getOldActiveNoteIndexBeforeDelete();
        if (oldActiveNoteIndexBeforeDelete > 0) {
            mainActivity.getCardSourceImplement().setOldActiveNoteIndexBeforeDelete(0);
            // Загрузка фрагмента c текстом TextFragment
            navigation.addFragment(TextFragment.newInstance(oldActiveNoteIndexBeforeDelete, false), R.id.text_container, false);
/*            requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.text_container, TextFragment.newInstance(oldActiveNoteIndexBeforeDelete, false))
                .commit();*/
        }
    }

    // Настройки адаптера listNotesAdapter
    private void setUpListNotesAdapter(MainActivity mainActivity) {
        listNotesAdapter = new ListNotesAdapter(mainActivity.getCardSourceImplement().getListNotes(), getResources().getConfiguration().orientation, this);

        // Вешаем обработчики событий при нажатии на имя заметки
        listNotesAdapter.setOnListNotesFragmentOnClickListener_name(new ListNotesFragmentOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    mainActivity.getCardSourceImplement().addCardNote();
                    // Перезапуск фрагмента со списком для отображения новой заметки
                    updateListNotes();

                    // Загрузка фрагмента c текстом TextFragment
                    indexChoisedElement = 1;
                    mainActivity.getCardSourceImplement().setActiveNoteIndex(1);
                    updateTextNote(indexChoisedElement);
                } else {
                    mainActivity.getCardSourceImplement().setActiveNoteIndex(position);
                    // Загрузка фрагмента c текстом TextFragment
                    updateTextNote(position);
                }
            }
        });

        // Вешаем обработчики событий при нажатии на дату заметки
        listNotesAdapter.setOnListNotesFragmentOnClickListener_date(new ListNotesFragmentOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Показать DatePicker для изменения даты заметки
                showDatePicker(position, mainActivity);
            }
        });
    }

    // Показать DatePicker
    private void showDatePicker(int sendedIndex, MainActivity mainActivity) {
        // Устанавливаем новую дату
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newYear = year;
                newMonth = monthOfYear + 1;
                newDay = dayOfMonth;
                mainActivity.getCardSourceImplement().setCardNote(sendedIndex, newYear, newMonth, newDay);
                listNotesAdapter.setListNotesDate(sendedIndex, mainActivity.getCardSourceImplement().getCardNote(sendedIndex).getDate());
                listNotesAdapter.notifyItemChanged(sendedIndex);
            }
        };
        // Отображаем диалоговое окно для выбора даты
        if (sendedIndex <= 0) {
            new DatePickerDialog(getContext(), datePickerDialog,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        } else {
            new DatePickerDialog(getContext(), datePickerDialog,
                    mainActivity.getCardSourceImplement().getCardNote(sendedIndex).getDateYear(),
                    mainActivity.getCardSourceImplement().getCardNote(sendedIndex).getDateMonth() - 1,
                    mainActivity.getCardSourceImplement().getCardNote(sendedIndex).getDateDay())
                    .show();
        }
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

    // Создание контекстного меню для элементов списка
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        MainActivity mainActivity = ((MainActivity) getActivity());
        int position = listNotesAdapter.getMenuContextClickPosition();
        switch (item.getItemId()) {
            case R.id.context_menu_action_show_card:
                // Отображение карточки заметки через контекстное меню
                mainActivity.getCardSourceImplement().setActiveNoteIndex(position);
                editCardFragment = new EditCardFragment();
                editCardFragment.show(getActivity().getFragmentManager(), "");
                break;
            case R.id.context_menu_action_delete_card:
                // Удаление карточки через контекстное меню
                // Отображение диалогового фрагмента DeleteFragment с подтверждением удаления заметки из контекстного меню
                DeleteFragment deleteFragment = new DeleteFragment().newInstance(true, position);
                deleteFragment.show(requireActivity().getFragmentManager(), "");
                break;
        }
        return super.onContextItemSelected(item);
    }

    // Отображение фрагмента с обновлённым списком заметок
    private void updateListNotes() {
        navigation.addFragment(ListNotesFragment.newInstance(), R.id.list_container, false);
/*
        requireActivity()
            .getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.list_container, ListNotesFragment.newInstance())
            .commit();
*/
    }

    // Отображение фрагмента с текстом заметки
    private void updateTextNote(int index) {
        navigation.addFragment(TextFragment.newInstance(index, true), R.id.text_container, false);
/*
        requireActivity()
            .getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.text_container, TextFragment.newInstance(index, true))
            .commit();
*/
    }
}