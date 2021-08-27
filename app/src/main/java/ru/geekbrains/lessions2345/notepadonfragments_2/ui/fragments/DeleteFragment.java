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
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.TimeZone;

import ru.geekbrains.lessions2345.notepadonfragments_2.R;
import ru.geekbrains.lessions2345.notepadonfragments_2.logic.CardNote;
import ru.geekbrains.lessions2345.notepadonfragments_2.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments_2.model.DeleteAnswersTypes;
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.Publisher;
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.PublisherGetter;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.MainActivity;

public class DeleteFragment extends DialogFragment implements OnClickListener {

    private Button buttonYes;
    private Button buttonNo;
    private Publisher publisher = new Publisher();
    boolean deleteNoteFromContextMenu = false;
    int indexChoosedNoteInContextMenu = 0;

    public static DeleteFragment newInstance(boolean deleteNoteFromContextMenu, int position) {
        DeleteFragment deleteFragment = new DeleteFragment();
        deleteFragment.setDeleteNoteFromContextMenu(deleteNoteFromContextMenu);
        deleteFragment.setIndexChoosedNoteInContextMenu(position);
        return deleteFragment;
    }

    public void setIndexChoosedNoteInContextMenu(int indexChoosedNoteInContextMenu) {
        this.indexChoosedNoteInContextMenu = indexChoosedNoteInContextMenu;
    }

    public void setDeleteNoteFromContextMenu(boolean deleteNoteFromContextMenu) {
        this.deleteNoteFromContextMenu = deleteNoteFromContextMenu;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_yes_no, null);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Получаем паблишер, чтобы передать с ним в MainActivity результат выбора пользователя по удалению заметки
        publisher = ((PublisherGetter) context).getPublisher();
    }

    private void initView(View view) {
        buttonYes = view.findViewById(R.id.button_yes);
        buttonYes.setOnClickListener(this::onYes);
        buttonNo = view.findViewById(R.id.button_no);
        buttonNo.setOnClickListener(this::onNo);
    }

    // Результат нажатия на кнопку отмены действия
    private void onNo(View view) {
        publisher.notifySingle(DeleteAnswersTypes.NO);
        dismiss();
    }

    // Результат нажатия на кнопку подтверждения действия
    public void onYes(View v) {
        if (deleteNoteFromContextMenu == false) {
            publisher.notifySingle(DeleteAnswersTypes.YES);
        } else {
            deleteNoteFromContextMenu = false;
            publisher.notifySingle(DeleteAnswersTypes.YES, indexChoosedNoteInContextMenu);
        }
        dismiss();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
}