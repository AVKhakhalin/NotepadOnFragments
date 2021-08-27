package ru.geekbrains.lessions2345.notepadonfragments_2.observe;

import ru.geekbrains.lessions2345.notepadonfragments_2.model.DeleteAnswersTypes;

public interface Observer {
    void completeGoogleAuthorise(boolean completeGoogleAuthorise);

    void updateDatesFromFireBase(int numberDownloadedNotes);

    void updateUserChooseDeleteFile(DeleteAnswersTypes userDecision);

    void updateUserChooseDeleteFileFromContextMenu(DeleteAnswersTypes userDecision, int indexChoosedNoteInContextMenu);
}
