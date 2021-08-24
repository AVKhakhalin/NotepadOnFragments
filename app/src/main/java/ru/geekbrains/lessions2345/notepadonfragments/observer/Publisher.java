package ru.geekbrains.lessions2345.notepadonfragments.observer;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.lessions2345.notepadonfragments.logic.CardNote;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class Publisher {
    // Обозреватели
    private List<Observer> observers;
    private String mainActivityName;

    CardNote cardNoteInitial = null;
    boolean createNewNoteModeInitial = false;
    int activeNoteIndexInitial = 0;
    boolean deleteModeInitial = false;
    int oldActiveNoteIndexBeforeDeleteInitial = 0;

    public Publisher(Activity mainActivity) {
        observers = new ArrayList<>();
        mainActivityName = mainActivity.getClass().getSimpleName();
    }

    // Подписаться к наблюдателю
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    // Отписатся от наблюдателя
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    // Отписать всех наблюдателей
    public void unsubscribeAll(Observer observer) {
        observers.clear();
    }

    // Разослать событие
    public void notify(CardNote cardNote, boolean createNewNoteMode, int activeNoteIndex, boolean deleteMode, int oldActiveNoteIndexBeforeDelete, String className) {
        if ((cardNote == null) && (activeNoteIndex == -1)) {
            for (Observer observer : observers) {
                observer.updateState(cardNote, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, className);
                if (observer.getClass().getSimpleName().equals(className) == true) {
                    observer.updateState(cardNoteInitial, createNewNoteModeInitial, activeNoteIndexInitial, deleteModeInitial, oldActiveNoteIndexBeforeDeleteInitial, className);
                }
            }
        } else {
            for (Observer observer : observers) {
                observer.updateState(cardNote, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, className);
                if (observer.getClass().getSimpleName().equals(mainActivityName) == true) {
                    cardNoteInitial = cardNote;
                    createNewNoteModeInitial = createNewNoteMode;
                    activeNoteIndexInitial = activeNoteIndex;
                    deleteModeInitial = deleteMode;
                    oldActiveNoteIndexBeforeDeleteInitial = oldActiveNoteIndexBeforeDelete;
                    break;
                }
            }
        }
    }
    /*public void notify(CardNote cardNote, boolean createNewNoteMode, int activeNoteIndex, boolean deleteMode, int oldActiveNoteIndexBeforeDelete) {
        createNewNoteModeInitial = createNewNoteMode;
        activeNoteIndexInitial = activeNoteIndex;
        deleteModeInitial = deleteMode;
        oldActiveNoteIndexBeforeDeleteInitial = oldActiveNoteIndexBeforeDelete;
        if ((cardNote == null) && (activeNoteIndex > -1) && (oldActiveNoteIndexBeforeDelete > -1)) {
            for (Observer observer : observers) {
//                observer.updateState(cardNote, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, "");
                observer.updateState(cardNote, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, "");
                if (observer.getClass().getSimpleName().equals(mainActivityName) == false) {
//                    observer.updateState(cardNoteInitial, createNewNoteModeInitial, activeNoteIndexInitial, deleteModeInitial, oldActiveNoteIndexBeforeDeleteInitial, "");
                    observer.updateState(cardNoteInitial, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, "");
                }
            }
        } else {
            for (Observer observer : observers) {
                observer.updateState(cardNote, createNewNoteMode, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete, "");
                if (observer.getClass().getSimpleName().equals(mainActivityName) == true) {
                    cardNoteInitial = cardNote;
                    createNewNoteModeInitial = createNewNoteMode;
                    activeNoteIndexInitial = activeNoteIndex;
                    deleteModeInitial = deleteMode;
                    oldActiveNoteIndexBeforeDeleteInitial = oldActiveNoteIndexBeforeDelete;
                    break;
                }
            }
        }
    }*/
}
