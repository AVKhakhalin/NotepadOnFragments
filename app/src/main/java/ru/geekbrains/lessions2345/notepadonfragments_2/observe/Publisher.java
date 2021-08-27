package ru.geekbrains.lessions2345.notepadonfragments_2.observe;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers;   // Все обозреватели

    public Publisher() {
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
            unsubscribe(observer);
        }
    }

    // Разослать событие об успешной загрузке новых данных с Firebase
    public void notifySingle(int numberDownloadedNotes) {
        for (Observer observer : observers) {
            observer.updateDatesFromFireBase(numberDownloadedNotes);
            unsubscribe(observer);
        }
    }
}
