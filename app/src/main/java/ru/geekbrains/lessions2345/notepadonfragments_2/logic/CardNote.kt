package ru.geekbrains.lessions2345.notepadonfragments_2.logic

import android.os.Parcel
import android.os.Parcelable

class CardNote (name : String, description : String, text : String, dateYear : Int, dateMonth : Int, dateDay : Int) : Parcelable {
    // Id заметки
    private var id : String = ""
    // Имя заметки
    private var name : String = ""
    // Краткое описание заметки
    private var description : String = ""
    // Текст заметки
    private var text : String = ""
    // Год создания заметки
    private var dateYear : Int = 0
    // Месяц создания заметки
    private var dateMonth : Int = 0
    // День создания заметки
    private var dateDay : Int = 0

    init {
        this.name = name
        this.description = description
        this.text = text
        this.dateYear = dateYear
        this.dateMonth = dateMonth
        this.dateDay = dateDay
    }

    public fun getId() : String {
        return id
    }

    public fun setId(id : String) {
        this.id = id
    }

    public fun getDate() : String {
        return "${if (dateDay < 10) "0" else ""}${dateDay}.${if (dateMonth < 10) "0" else ""}${dateMonth}.${dateYear}"
    }

    public fun getName() : String {
        return name
    }

    public fun setName(name : String) {
        this.name = name
    }

    public fun getDescription() : String {
        return description
    }

    public fun setDescription(description : String) {
        this.description = description
    }

    public fun getText() : String {
        return text
    }

    public fun setText(text : String) {
        this.text = text
    }

    public fun getDateYear() : Int {
        return dateYear
    }

    public fun setDateYear(dateYear : Int) {
        this.dateYear = dateYear
    }

    public fun getDateMonth() : Int {
        return dateMonth
    }

    public fun setDateMonth(dateMonth : Int) {
        this.dateMonth = dateMonth
    }

    public fun getDateDay() : Int {
        return dateDay
    }

    public fun setDateDay(dateDay : Int) {
        this.dateDay = dateDay
    }

    // МЕТОДЫ ДЛЯ ПЕРЕДАЧИ КЛАССА
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
        id = parcel.readString().toString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest != null) {
            dest.writeString(name)
            dest.writeString(description)
            dest.writeString(text)
            dest.writeInt(dateYear)
            dest.writeInt(dateMonth);
            dest.writeInt(dateDay);
            dest.writeString(id)
        }
    }

    companion object CREATOR : Parcelable.Creator<CardNote> {
        override fun createFromParcel(parcel: Parcel): CardNote {
            return CardNote(parcel)
        }

        override fun newArray(size: Int): Array<CardNote?> {
            return arrayOfNulls(size)
        }
    }
}