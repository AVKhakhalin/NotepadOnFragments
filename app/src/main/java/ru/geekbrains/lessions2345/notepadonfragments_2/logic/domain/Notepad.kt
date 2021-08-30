package ru.geekbrains.lessions2345.notepadonfragments_2.logic.domain

import android.os.Parcel
import android.os.Parcelable
import ru.geekbrains.lessions2345.notepadonfragments_2.logic.CardNote
import ru.geekbrains.lessions2345.notepadonfragments_2.model.Constants
import java.util.*

class Notepad() : Parcelable {
    /* ВАЖНО!!!
    Нулевой элемент всегда задан по-умолчанию!
    Поэтому работа с классом осуществляется с 1 элемента.
    Соответственно, индекс элемента будет и его порядковым номером для обращения*/

    /* Интервал задания новых.
    Сначала задаются 10 пустых элементов.
    Как только будут заданы все 10 элементов, то добавятся ещё 10 пустых элементов.
    И так далее.
    Сделано для того, чтобы лишний раз не переписывать массив при копировании
    В каком-то роде, это буфер пустых элементов для ускорения работы с данным классом
    К сожалению, метод Parcelable не умеет работать с ArrayList<Integer>, поэтому приходится применять смешанные типы данных */
    // Начальный размер массива
    private val START_SIZE_INTARRAYS = Constants.DELTA_CHANGE_INT_ARRAYS

    // Список id заметок
    private var id = ArrayList<String>()
    // Список имен заметок
    private var name = ArrayList<String>()
    // Список кратких описаний заметок
    private var description = ArrayList<String>()
    // Список текстов заметок
    private var text = ArrayList<String>()
    // Массив годов создания заметок
    private var dateYear = Array<Int>(1){0}
    // Массив месяцев создания заметок
    private var dateMonth = Array<Int>(1){0}
    // Массив дней создания заметок
    private var dateDay = Array<Int>(1){0}

    init {
        id.add("")
        name.add(Constants.NAME_EMPTY_NOTE_ADD)
        description.add(Constants.NAME_EMPTY_NOTE_ADD)
        text.add("")
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        dateYear[0] = calendar.get(Calendar.YEAR)
        dateMonth[0] = calendar.get(Calendar.MONTH) + 1
        dateDay[0] = calendar.get(Calendar.DAY_OF_MONTH)
    }

    // Вывести список всех дат записей, безу учёта нулевого элемента
    public fun getAllDates() : String {
        var result = ""
        for (i in 1..getNumberElements()) {
            result += "${if (dateDay[i] < 10) "0" else ""}${dateDay[i]}.${if (dateMonth[i] < 10) "0" else ""}${dateMonth[i]}.${dateYear[i]}";
        }
        return result
    }

    // Вывести список всех имен записей, без учёта нулевого элемента
    public fun getAllNames(): String {
        var result = ""
        for (i in 1..getNumberElements()) {
            result += "${name.get(i)}\n"
        }
        return result
    }

    // Вывести количество элементов без учёта нулевого элемента, всегда присутствующего по-умолчанию
    public fun getNumberElements(): Int {
        return name.size - 1
    }

    // Добавить новый элемент в виде имени и описания заметки
    public fun add(newName: String, newDescription: String) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        var numberCurElement = name.size
        if (newName.equals("")) {
            name.add(Constants.NAME_EMPTY_NOTE)
        } else {
            name.add(newName)
        }
        if (newDescription.equals("")) {
            if (newName.equals("")) {
                description.add(Constants.DESCRIPTION_EMPTY_NOTE)
            } else {
                description.add(newName)
            }
        } else {
            description.add(newDescription)
        }
        id.add("")
        text.add("")
        // Проверка на превышение размера списков над размерами массивов
        if (numberCurElement < dateYear.size) {
            dateYear[numberCurElement] = calendar.get(Calendar.YEAR)
            dateMonth[numberCurElement] = calendar.get(Calendar.MONTH) + 1
            dateDay[numberCurElement] = calendar.get(Calendar.DAY_OF_MONTH)
        } else {
            var curSizeDateArray = dateYear.size
            var tempArrayYear = Array<Int>(curSizeDateArray){0}
            var tempArrayMonth = Array<Int>(curSizeDateArray){0}
            var tempArrayDay = Array<Int>(curSizeDateArray){0}
            for (i in 0..curSizeDateArray - 1) {
                tempArrayYear[i] = dateYear[i]
                tempArrayMonth[i] = dateMonth[i]
                tempArrayDay[i] = dateDay[i]
            }
            dateYear = Array<Int>(numberCurElement + Constants.DELTA_CHANGE_INT_ARRAYS){0}
            dateMonth = Array<Int>(numberCurElement + Constants.DELTA_CHANGE_INT_ARRAYS){0}
            dateDay = Array<Int>(numberCurElement + Constants.DELTA_CHANGE_INT_ARRAYS){0}
            curSizeDateArray = tempArrayYear.size
            for (i in 0..curSizeDateArray - 1) {
                dateYear[i] = tempArrayYear[i]
                dateMonth[i] = tempArrayMonth[i]
                dateDay[i] = tempArrayDay[i]
            }
            dateYear[numberCurElement] = calendar.get(Calendar.YEAR)
            dateMonth[numberCurElement] = calendar.get(Calendar.MONTH) + 1
            dateDay[numberCurElement] = calendar.get(Calendar.DAY_OF_MONTH)
        }
        // Ранжирование заметок: последняя заметка становится первой, а первая заметка - последней
        numberCurElement = getNumberElements()
        var firstId: String = id.get(numberCurElement)
        var firstName: String = name.get(numberCurElement)
        var firstDescription: String = description.get(numberCurElement)
        var firstText: String = text.get(numberCurElement)
        var firstDateYear = dateYear[numberCurElement]
        var firstDateMonth = dateMonth[numberCurElement]
        var firstDateDay = dateDay[numberCurElement]
        for (i in numberCurElement - 1 downTo 1 step 1) {
            id.set(i + 1, id.get(i))
            name.set(i + 1, name.get(i))
            description.set(i + 1, description.get(i))
            text.set(i + 1, text.get(i))
            dateYear[i + 1] = dateYear[i]
            dateMonth[i + 1] = dateMonth[i]
            dateDay[i + 1] = dateDay[i]
        }
        id.set(1, firstId)
        name.set(1, firstName)
        description.set(1, firstDescription)
        text.set(1, firstText)
        dateYear[1] = firstDateYear
        dateMonth[1] = firstDateMonth
        dateDay[1] = firstDateDay
    }

    // Добавить новый элемент в виде карточки CrdNote
    public fun add(newCardNote: CardNote) {
        var numberCurElement = name.size
        id.add(newCardNote.getId())
        name.add(newCardNote.getName())
        description.add(newCardNote.getDescription())
        text.add(newCardNote.getText())
        // Проверка на превышение размера списков над размерами массивов
        if (numberCurElement < dateYear.size) {
            dateYear[numberCurElement] = newCardNote.getDateYear()
            dateMonth[numberCurElement] = newCardNote.getDateMonth()
            dateDay[numberCurElement] = newCardNote.getDateDay()
        } else {
            var numberCurDateElement: Int = dateYear.size
            var tempArrayYear = Array<Int>(numberCurDateElement){0}
            var tempArrayMonth = Array<Int>(numberCurDateElement){0}
            var tempArrayDay = Array<Int>(numberCurDateElement){0}
            for (i in 0..numberCurDateElement - 1) {
                tempArrayYear[i] = dateYear[i]
                tempArrayMonth[i] = dateMonth[i]
                tempArrayDay[i] = dateDay[i]
            }
            dateYear = Array<Int>(numberCurElement + Constants.DELTA_CHANGE_INT_ARRAYS){0}
            dateMonth = Array<Int>(numberCurElement + Constants.DELTA_CHANGE_INT_ARRAYS){0}
            dateDay = Array<Int>(numberCurElement + Constants.DELTA_CHANGE_INT_ARRAYS){0}
            for (i in 0..tempArrayYear.size - 1) {
                dateYear[i] = tempArrayYear[i]
                dateMonth[i] = tempArrayMonth[i]
                dateDay[i] = tempArrayDay[i]
            }
            dateYear[numberCurElement] = newCardNote.getDateYear()
            dateMonth[numberCurElement] = newCardNote.getDateMonth()
            dateDay[numberCurElement] = newCardNote.getDateDay()
        }
        // Ранжирование заметок: последняя заметка становится первой, а первая заметка - последней
        var firstId: String = id.get(getNumberElements())
        var firstName: String = name.get(getNumberElements())
        var firstDescription: String = description.get(getNumberElements())
        var firstText: String = text.get(getNumberElements())
        var firstDateYear: Int = dateYear[getNumberElements()]
        var firstDateMonth: Int = dateMonth[getNumberElements()]
        var firstDateDay: Int = dateDay[getNumberElements()]
        for (i in getNumberElements() - 1 downTo 1 step 1) {
            id.set(i + 1, id.get(i))
            name.set(i + 1, name.get(i))
            description.set(i + 1, description.get(i))
            text.set(i + 1, text.get(i))
            dateYear[i + 1] = dateYear[i]
            dateMonth[i + 1] = dateMonth[i]
            dateDay[i + 1] = dateDay[i]
        }
        id.set(1, firstId)
        name.set(1, firstName)
        description.set(1, firstDescription)
        text.set(1, firstText)
        dateYear[1] = firstDateYear
        dateMonth[1] = firstDateMonth
        dateDay[1] = firstDateDay
    }

    // Удалить запись
    public fun remove(index: Int) {
        if (index > 0) {
            id.removeAt(index)
            name.removeAt(index)
            description.removeAt(index)
            text.removeAt(index)

            var tempArrayYear = Array<Int>(dateYear.size - 1){0}
            var tempArrayMonth = Array<Int>(dateYear.size - 1){0}
            var tempArrayDay = Array<Int>(dateYear.size - 1){0}

            var counter: Int = 0

            for (i in 0..dateYear.size - 1) {
                if (i != index) {
                    tempArrayYear[counter] = dateYear[i]
                    tempArrayMonth[counter] = dateMonth[i]
                    tempArrayDay[counter] = dateDay[i]
                    counter++;
                }
            }
            dateYear = Array<Int>(tempArrayYear.size){0}
            dateMonth = Array<Int>(tempArrayYear.size){0}
            dateDay = Array<Int>(tempArrayYear.size){0}
            for (i in 0..dateYear.size - 1) {
                dateYear[i] = tempArrayYear[i]
                dateMonth[i] = tempArrayMonth[i]
                dateDay[i] = tempArrayDay[i]
            }
        }
    }

    // Получить имя заметки
    public fun getName(index: Int): String {
        if ((index >= 0) && (index <= getNumberElements())) {
            return name.get(index)
        } else {
            return ""
        }
    }

    // Установить имя заметки
    public fun setName(index: Int, newName: String) {
        if ((index > 0) && (index <= getNumberElements())) {
            name.set(index, newName)
        }
    }

    // Вывести список всех кратких имен записей, без учёта нулевого элемента
    public fun getAllDescription(): String {
        var result: String = ""
        for (i in 1..getNumberElements()) {
            result += description.get(i) + "\n"
        }
        return result
    }

    // Вывести краткое имя записи
    public fun getDescription(index: Int): String {
        if ((index > 0) && (index <= getNumberElements())) {
            return description.get(index)
        } else {
            return ""
        }
    }

    // Установить краткое описание заметки
    public fun setDescription(index: Int, newDescription: String) {
        if ((index > 0) && (index <= getNumberElements())) {
            description.set(index, newDescription)
        }
    }

    // Получить текст заметки
    public fun getText(index: Int): String {
        if ((index > 0) && (index <= getNumberElements())) {
            return text.get(index)
        } else {
            return ""
        }
    }

    // Установить текст заметки
    public fun setText(index: Int, newText: String) {
        if ((index > 0) && (index <= getNumberElements())) {
            text.set(index, newText)
        }
    }

    // Получить дату заметки
    public fun getDate(index: Int): String {
        if ((index > 0) && (index <= getNumberElements())) {
            return "${if (dateDay[index] < 10) "0" else ""}${dateDay[index]}.${if (dateMonth[index] < 10) "0" else ""}${dateMonth[index]}.${dateYear[index]}"
        } else {
            return ""
        }
    }

    // Установить год заметки
    public fun setDateYear(index: Int, newDateYear: Int) {
        if ((index > 0) && (index <= getNumberElements())) {
            dateYear[index] = newDateYear
        }
    }

    // Получить год заметки
    public fun getDateYear(index: Int): Int {
        if ((index > 0) && (index <= getNumberElements())) {
            return dateYear[index]
        } else {
            return -1
        }
    }

    // Получить месяц заметки
    public fun getDateMonth(index: Int): Int {
        if ((index > 0) && (index <= getNumberElements())) {
            return dateMonth[index]
        } else {
            return -1
        }
    }

    // Установить месяц заметки
    public fun setDateMonth(index: Int, newDateMonth: Int) {
        if ((index > 0) && (index <= getNumberElements())) {
            dateMonth[index] = newDateMonth
        }
    }

    // Получить день заметки
    public fun getDateDay(index: Int): Int {
        if ((index > 0) && (index <= getNumberElements())) {
            return dateDay[index]
        } else {
            return -1
        }
    }

    // Установить день заметки
    public fun setDateDay(index: Int, newDateDay: Int) {
        if ((index > 0) && (index <= getNumberElements())) {
            dateDay[index] = newDateDay
        }
    }

    // Получение id заметки
    public fun getId(index: Int): String {
        if ((index > 0) && (index <= getNumberElements())) {
            return id.get(index)
        } else {
            return ""
        }
    }

    // Установка id заметке
    public fun setId(index: Int, newId: String) {
        if ((index > 0) && (index <= getNumberElements())) {
            id.set(index, newId)
        }
    }

    // МЕТОДЫ ДЛЯ ПЕРЕДАЧИ КЛАССА
    constructor(parcel: Parcel) : this() {
        id = parcel.createStringArrayList() as ArrayList<String>
        name = parcel.createStringArrayList() as ArrayList<String>
        description = parcel.createStringArrayList() as ArrayList<String>
        text = parcel.createStringArrayList() as ArrayList<String>
        dateYear = parcel.createIntArray() as Array<Int>
        dateMonth = parcel.createIntArray() as Array<Int>
        dateDay = parcel.createIntArray() as Array<Int>
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest != null) {
            dest.writeStringList(id)
            dest.writeStringList(name)
            dest.writeStringList(description)
            dest.writeStringList(text)
            dest.writeArray(dateYear)
            dest.writeArray(dateMonth);
            dest.writeArray(dateDay);
        }
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Notepad> {
        override fun createFromParcel(parcel: Parcel): Notepad {
            return Notepad(parcel)
        }

        override fun newArray(size: Int): Array<Notepad?> {
            return arrayOfNulls(size)
        }
    }
}