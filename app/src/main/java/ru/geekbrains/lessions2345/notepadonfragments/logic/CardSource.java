package ru.geekbrains.lessions2345.notepadonfragments.logic;

public interface CardSource {
    int size();
    CardNote getCardNote(int position);
}
