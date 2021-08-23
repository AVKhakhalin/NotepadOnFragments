package ru.geekbrains.lessions2345.notepadonfragments.observer;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.lessions2345.notepadonfragments.logic.CardNote;

public class Publisher {
    // Обозреватели
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
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
    public void notify(CardNote cardNote, int activeNoteIndex, boolean deleteMode, int oldActiveNoteIndexBeforeDelete) {
        for (Observer observer : observers) {
            observer.updateState(cardNote, activeNoteIndex, deleteMode, oldActiveNoteIndexBeforeDelete);
        }
    }
}
