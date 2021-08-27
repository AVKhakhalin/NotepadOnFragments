package ru.geekbrains.lessions2345.notepadonfragments_2.observe;

public interface Observer {
    void completeGoogleAuthorise(boolean completeGoogleAuthorise);
    void updateDatesFromFireBase(int numberDownloadedNotes);
}
