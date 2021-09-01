package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.lessions2345.notepadonfragments_2.R;
import ru.geekbrains.lessions2345.notepadonfragments_2.logic.ListNotes;

public class ListNotesAdapterOriginalJava extends RecyclerView.Adapter<ListNotesAdapterOriginalJava.ViewHolder> {

    private ListNotes listNotes;
    int orientation = 1;

    // Переменные для передачи событий во врагмент
    private ListNotesFragmentOnClickListener listener_name;
    private ListNotesFragmentOnClickListener listener_date;

    // Переменные для контекстного меню
    private Fragment fragment;
    private int menuContextClickPosition = 0;

    public int getMenuContextClickPosition() {
        return menuContextClickPosition;
    }

    public void setOnListNotesFragmentOnClickListener_name(ListNotesFragmentOnClickListener listener_name) {
        this.listener_name = listener_name;
    }

    public void setOnListNotesFragmentOnClickListener_date(ListNotesFragmentOnClickListener listener_date) {
        this.listener_date = listener_date;
    }

    public ListNotesAdapterOriginalJava(ListNotes listNotes, int orientation, Fragment fragment) {
        this.listNotes = listNotes;
        this.orientation = orientation;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (position == 0) {
                holder.textView_name.setText(listNotes.getName(position));
            } else {
                holder.textView_name.setText(listNotes.getDescription(position));
            }
        } else {
            holder.textView_name.setText(listNotes.getName(position));
        }
        holder.textView_date.setText(listNotes.getDate(position));
    }

    @Override
    public int getItemCount() {
        return listNotes.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name;
        TextView textView_date;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.name_note_text);
            textView_date = itemView.findViewById(R.id.date_note_text);

            // Регистрация стартового элемента для контекстного меню
            fragment.registerForContextMenu(textView_name);

            // Установка обработчиков событий для нажатия на имя заметки
            textView_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener_name.onClick(v, getAdapterPosition());
                }
            });
            textView_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuContextClickPosition = getAdapterPosition();
                    if (menuContextClickPosition > 0) {
                        textView_name.showContextMenu(0, 0);
                        return false;
                    } else {
                        return true;
                    }
                }
            });

            // Установка обработчика событий для нажатия на дату заметки
            textView_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener_date.onClick(v, getAdapterPosition());
                }
            });
        }
    }

    public void setListNotesName(int index, String name) {
        listNotes.setName(index, name);
    }

    public void setListNotesDescription(int index, String description) {
        listNotes.setDescription(index, description);
    }

    public void addListNote(int index, String name, String description, String date) {
        listNotes.addNote(index, name, description, date);
    }

    public void setListNotesDate(int index, String date) {
        listNotes.setDate(index, date);
    }
}
