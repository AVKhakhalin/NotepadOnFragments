package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.model.CONSTANTS;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class ListNotesFragment extends Fragment implements ListNotesFragmentOnClickListener {

    private int newYear;
    private int newMonth;
    private int newDay;

    private final String KEY_INDES_CHOISED_ELEMENT = "ChoisedElement";
    private int indexChoisedElement = 1;
    private CardView cardView = null;

    private ListNotesAdapter listNotesAdapter;

    private EditCardFragment editCardFragment;

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

        // Восстановление отображения содержимого просматриваемой заметки до удаления другой заметки через контекстное меню
        int oldActiveNoteIndexBeforDelete = ((MainActivity) getActivity()).getCardSourceImplement().getOldActiveNoteIndexBeforDelete();
        if (oldActiveNoteIndexBeforDelete > 0) {
            ((MainActivity) getActivity()).getCardSourceImplement().setOldActiveNoteIndexBeforDelete(0);
            // Загрузка фрагмента c текстом TextFragment
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.text_container, TextFragment.newInstance(oldActiveNoteIndexBeforDelete, false))
                    .commit();
        }

        return view;
    }

    // Метод для установки списка заметок к отображению и реагированию на события
    private void listNotesSetup(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_list_container);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        listNotesAdapter = new ListNotesAdapter(((MainActivity) getActivity()).getCardSourceImplement().getListNotes(), getResources().getConfiguration().orientation, this);

        // Вешаем обработчики событий при нажатии на имя заметки
        listNotesAdapter.setOnListNotesFragmentOnClickListener_name(new ListNotesFragmentOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    ((MainActivity) getActivity()).getCardSourceImplement().addCardNote();

                    // Перезапуск фрагмента со списком для отображения новой заметки
                    updateListNotes();

                    // Загрузка фрагмента c текстом TextFragment
                    indexChoisedElement = 1;
                    ((MainActivity) getActivity()).getCardSourceImplement().setActiveNoteIndex(1);
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.text_container, TextFragment.newInstance(indexChoisedElement, true))
                            .commit();
                } else {
                    ((MainActivity) getActivity()).getCardSourceImplement().setActiveNoteIndex(position);

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
                showDatePicker(position, ((MainActivity) getActivity()));
            }
        });
        recyclerView.setAdapter(listNotesAdapter);
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
                listNotesAdapter.setListNotesDate(sendedIndex, ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(sendedIndex).getDate());
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
                    ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(sendedIndex).getDateYear(),
                    ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(sendedIndex).getDateMonth() - 1,
                    ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(sendedIndex).getDateDay())
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
        int position = listNotesAdapter.getMenuContextClickPosition();
        switch (item.getItemId()) {
            case R.id.context_menu_action_show_card:
                // Отображение карточки заметки через контекстное меню
                ((MainActivity) getActivity()).getCardSourceImplement().setActiveNoteIndex(position);
                editCardFragment = new EditCardFragment();
                editCardFragment.show(getActivity().getFragmentManager(), "");
                break;
            case R.id.context_menu_action_delete_card:
                // Удаление карточки через контекстное меню
                int oldActiveNoteIndex = ((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex();
                if (oldActiveNoteIndex > 0) {
                    ((MainActivity) getActivity()).getCardSourceImplement().setActiveNoteIndex(position);
                    // Удаление заметки
                    String deletedNoteName = "\"" + ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex()).getName() + "\" (\"" + ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex()).getDescription() + "\")";
                    ((MainActivity) getActivity()).getCardSourceImplement().removeCardNote(((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex());
                    Toast.makeText(getContext(), "Заметка " + deletedNoteName + " удалена.", Toast.LENGTH_SHORT).show();

                    if (oldActiveNoteIndex != position) {
                        if (oldActiveNoteIndex > position) {
                            oldActiveNoteIndex--;
                            ((MainActivity) getActivity()).getCardSourceImplement().setOldActiveNoteIndexBeforDelete(oldActiveNoteIndex);
                        } else {
                            ((MainActivity) getActivity()).getCardSourceImplement().setOldActiveNoteIndexBeforDelete(oldActiveNoteIndex);
                        }
                    } else {
                        ((MainActivity) getActivity()).getCardSourceImplement().setActiveNoteIndex(0);
                        ((MainActivity) getActivity()).getCardSourceImplement().setOldActiveNoteIndexBeforDelete(0);
                        // Отображение пустого текстового поля
                        ((MainActivity) getActivity()).getCardSourceImplement().setDeleteMode(true);
                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.text_container, new Fragment())
                                .commitNow();
                        ((MainActivity) getActivity()).getCardSourceImplement().setDeleteMode(false);
                    }
                    updateListNotes();
                } else if ((oldActiveNoteIndex == 0) && (((MainActivity) getActivity()).getCardSourceImplement().size() > 0)) {
                    ((MainActivity) getActivity()).getCardSourceImplement().setActiveNoteIndex(position);
                    // Удаление заметки
                    String deletedNoteName = "\"" + ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex()).getName() + "\" (\"" + ((MainActivity) getActivity()).getCardSourceImplement().getCardNote(((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex()).getDescription() + "\")";
                    ((MainActivity) getActivity()).getCardSourceImplement().removeCardNote(((MainActivity) getActivity()).getCardSourceImplement().getActiveNoteIndex());
                    Toast.makeText(getContext(), "Заметка " + deletedNoteName + " удалена.", Toast.LENGTH_SHORT).show();

                    ((MainActivity) getActivity()).getCardSourceImplement().setActiveNoteIndex(0);
                    ((MainActivity) getActivity()).getCardSourceImplement().setOldActiveNoteIndexBeforDelete(0);
                    updateListNotes();
                }

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void updateListNotes() {
        // Отображение фрагмента с обновлённым списком заметок
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, ListNotesFragment.newInstance())
                .commit();
    }
}