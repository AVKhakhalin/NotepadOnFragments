package ru.geekbrains.lessions2345.notepadonfragments_2.logic

// Интерфейс, отвечающий за получение информации с сервера
interface CardSourceResponse {
    fun initialized(cardsSource: CardSource)
}