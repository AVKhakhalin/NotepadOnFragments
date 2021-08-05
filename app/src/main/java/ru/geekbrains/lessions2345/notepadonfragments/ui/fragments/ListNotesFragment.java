package ru.geekbrains.lessions2345.notepadonfragments.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class ListNotesFragment extends Fragment {

    Notepad notepad;

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
        Toast.makeText(getContext(), String.valueOf(getArguments()), Toast.LENGTH_SHORT).show();
        if (getArguments() != null) {
            this.notepad = getArguments().getParcelable(MainActivity.KEY_NOTEPAD);
            Toast.makeText(getContext(), "Передача notepad во фрагмент ListNotesFragment прошла", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        LinearLayout linearLayout = (LinearLayout) view;

        // Установка значения текстового поля
        for (int i = 0; i <= notepad.getNumberElements(); i++) {
            TextView textView = new TextView(getContext());
            if (i > 0) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textView.setText(i + "." + notepad.getDescription(i) + "\n" + notepad.getDate(i));
                } else {
                    textView.setText(i + "." + notepad.getName(i) + "\n" + notepad.getDate(i));
                }
            } else {
                textView.setText(notepad.getName(i) + "\n");
            }
            // Форматирование текстового поля
            textView.setTextSize(20);
            linearLayout.addView(textView);
            int sendedIndex = i;
            textView.setOnClickListener(new View.OnClickListener() {
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
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MainActivity.KEY_NOTEPAD, notepad);
        super.onSaveInstanceState(outState);
    }
}
