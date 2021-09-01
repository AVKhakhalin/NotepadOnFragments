package ru.geekbrains.lessions2345.notepadonfragments_2.observe

import ru.geekbrains.lessions2345.notepadonfragments_2.model.DeleteAnswersTypes
import java.util.ArrayList

class PublisherKotlin {
    // Все обозреватели
    private var observers : MutableList<Observer>? = null

    // Подписаться на паблишер
    fun subscribe(observer: Observer) {
        observers?.add(observer)
    }

    // Отписаться от паблишера
    fun unsubscribe(observer: Observer) {
        observers?.remove(observer)
    }

    // Разослать событие об успешной аутентификации через Google
    fun notifySingle(completeGoogleAuthorise : Boolean) {
        if (observers != null) {
            for (observer in observers!!) {
                observer.completeGoogleAuthorise(completeGoogleAuthorise)
            }
        }
    }

    // Разослать событие об успешной загрузке новых данных с Firebase
    fun notifySingle(numberDownloadedNotes: Int) {
        if (observers != null) {
            for (observer in observers!!) {
                observer.updateDatesFromFireBase(numberDownloadedNotes)
            }
        }
    }

    // Разослать событие о выборе пользователя при удалении файла из меню AppBar (нужно удалить только открытую заметку)
    fun notifySingle(userDecision: DeleteAnswersTypes) {
        if ((observers != null) && (userDecision != null)) {
            for (observer in observers!!) {
                observer.updateUserChooseDeleteFile(userDecision!!)
            }
        }
    }

    // Разослать событие о выборе пользователя при удалении файла из контекстного меню списка (нужно удалить выбранную заметку и отследить, чтобы текущая просматриваеая заметка не изменилась)
    fun notifySingle(userDecision: DeleteAnswersTypes, indexChoosedNoteInContextMenu: Int) {
        if ((observers != null) && (userDecision != null)) {
            for (observer in observers!!) {
                observer.updateUserChooseDeleteFileFromContextMenu(userDecision!!, indexChoosedNoteInContextMenu)
            }
        }
    }
}