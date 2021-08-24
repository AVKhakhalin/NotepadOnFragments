package ru.geekbrains.lessions2345.notepadonfragments.logic;

public interface CardSource {
    int size();

    CardNote getCardNote(int position);

    ListNotes getListNotes();

    void addCardNote();

    void addCardNote(CardNote cardNote);

    void setCardNote(int position, CardNote cardNote);
    void setCardNote(int position, String name, String description);
    void setCardNote(int position, String text);
    void setCardNote(int position, int year, int month, int day);
    void setCardNote(boolean deleteMode, int activeNoteIndex, boolean createNewNoteMode, int oldActiveNoteIndexBeforeDelete);

    void removeCardNote(int position);
}
