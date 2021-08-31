package ru.geekbrains.lessions2345.notepadonfragments_2.model

class Constants {
    companion object {
        @JvmField
        val LIST_NAMES_SIZE : Int = 23
        @JvmField
        val LIST_DATES_SIZE : Int = 17
        @JvmField
        val KEY_CARD_SOURCE_IMPLEMENT : String = "CardSourceImplement"
        @JvmField
        val KEY_INDEX : String = "Index"
        @JvmField
        val KEY_CREATED_NEW_NOTE : String = "CreatedNewNote"
        @JvmField
        val KEY_DATA_SETTINGS : String = "Settings"
        @JvmField
        val KEY_GOOGLE_AUTHORISE : String = "GoogleAuthorise"
        @JvmField
        val TEST_DATA_INT : Int = 0
        @JvmField
        val FILE_DATA_INT : Int = 1
        @JvmField
        val FIREBASE_DATA_INT : Int = 2
        @JvmField
        val DATABASE_DATA_INT : Int = 3

        // Размер буфера карточек заметок (для работы с массивами дат)
        @JvmField
        val DELTA_CHANGE_INT_ARRAYS : Int = 10

        // По-умолчанию, короткое название пустой записи
        @JvmField
        val NAME_EMPTY_NOTE_ADD : String = "СОЗДАТЬ ЗАМЕТКУ"
        @JvmField
        val NAME_EMPTY_NOTE : String = "НОВ.ЗАМ."
        @JvmField
        val DESCRIPTION_EMPTY_NOTE : String = "НОВАЯ ЗАМЕТКА"
    }
}