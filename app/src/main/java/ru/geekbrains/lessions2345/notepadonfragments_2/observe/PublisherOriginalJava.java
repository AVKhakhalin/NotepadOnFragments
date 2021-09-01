package ru.geekbrains.lessions2345.notepadonfragments_2.observe;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.lessions2345.notepadonfragments_2.model.DeleteAnswersTypes;

public class PublisherOriginalJava {
    // Все обозреватели
    private List<Observer> observers;

    public PublisherOriginalJava() {
        observers = new ArrayList<>();
    }

    // Подписать
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    // Отписать
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    // Разослать событие об успешной аутентификации через Google
    public void notifySingle(Boolean completeGoogleAuthorise) {
        for (Observer observer : observers) {
            observer.completeGoogleAuthorise(completeGoogleAuthorise);
        }
    }

    // Разослать событие об успешной загрузке новых данных с Firebase
    public void notifySingle(int numberDownloadedNotes) {
        for (Observer observer : observers) {
            observer.updateDatesFromFireBase(numberDownloadedNotes);
        }
    }

    // Разослать событие о выборе пользователя при удалении файла из меню AppBar (нужно удалить только открытую заметку)
    public void notifySingle(DeleteAnswersTypes userDecision) {
        for (Observer observer : observers) {
            observer.updateUserChooseDeleteFile(userDecision);
        }
    }

    // Разослать событие о выборе пользователя при удалении файла из контекстного меню списка (нужно удалить выбранную заметку и отследить, чтобы текущая просматриваеая заметка не изменилась)
    public void notifySingle(DeleteAnswersTypes userDecision, int indexChoosedNoteInContextMenu) {
        for (Observer observer : observers) {
            observer.updateUserChooseDeleteFileFromContextMenu(userDecision, indexChoosedNoteInContextMenu);
        }
    }
}
