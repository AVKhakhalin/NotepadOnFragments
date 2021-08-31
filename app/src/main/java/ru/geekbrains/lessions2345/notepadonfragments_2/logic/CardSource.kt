package ru.geekbrains.lessions2345.notepadonfragments_2.logic

interface CardSource {
    fun size(): Int

    fun getCardNote(position: Int): CardNote

    fun getListNotes(): ListNotes

    fun addCardNote()

    fun addCardNote(cardNote: CardNote)

    fun setCardNote(position: Int, cardNote: CardNote)

    fun setCardNote(position: Int, name: String, description: String)

    fun setCardNote(position: Int, text: String)

    fun setCardNote(position: Int, year: Int, month: Int, day: Int)

    fun removeCardNote(position: Int)
}