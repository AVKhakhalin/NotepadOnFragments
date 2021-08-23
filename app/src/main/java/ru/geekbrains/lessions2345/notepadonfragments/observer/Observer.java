package ru.geekbrains.lessions2345.notepadonfragments.observer;

import ru.geekbrains.lessions2345.notepadonfragments.logic.CardNote;
import ru.geekbrains.lessions2345.notepadonfragments.logic.ListNotes;

public interface Observer {
    void updateState(CardNote cardNote, int activeNoteIndex, boolean deleteMode, int oldActiveNoteIndexBeforeDelete);

    void updateState(ListNotes listNotes, int activeNoteIndex, boolean deleteMode, int oldActiveNoteIndexBeforeDelete);
}
