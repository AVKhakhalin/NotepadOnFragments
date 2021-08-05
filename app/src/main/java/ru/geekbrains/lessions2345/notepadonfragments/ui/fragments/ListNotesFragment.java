package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class ListNotesFragment extends Fragment {

    Notepad notepad;

    public static ListNotesFragment newInstance(Notepad notepad)
    {
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
        if (getArguments() != null)
        {
            this.notepad = getArguments().getParcelable(MainActivity.KEY_NOTEPAD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        LinearLayout linearLayout = (LinearLayout) view;

        // Считывание класса notepad

/*
        if (savedInstanceState != null)
        {
            notepad = this.getArguments().getParcelable(MainActivity.KEY_NOTEPAD);
            textView.setText(notepad.getName(1));
        }
        else
        {
            textView.setText("savedInstanceState is empty");
        }
*/

/*        // Получение класса notepad из MainActivity
        MainActivity mainActivity = (MainActivity) getActivity();
        notepad = mainActivity.getNotepad();*/

        // Установка значения текстового поля
        for (int i = 1; i <= notepad.getNumberElements(); i++)
        {
            TextView textView = new TextView(getContext());
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                textView.setText(notepad.getDescription(i));
            }
            else
            {
                textView.setText(notepad.getName(i));
            }
            // Форматирование текстового поля
            textView.setTextSize(20);
            linearLayout.addView(textView);
            textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v){
                    // Загрузка второго фрагмента
                }
            });
        }
        return view;
    }
}
