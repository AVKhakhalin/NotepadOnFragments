package ru.geekbrains.lessions2345.notepadonfragments_2.logic

class ListNotes (names : Array<String>, descriptions : Array<String>, dates : Array<String>) {
    // Список имён заметок
    private var names: Array<String> = Array<String>(1){""}

    // Список кратких описаний заметок
    private var descriptions: Array<String> = Array<String>(1){""}

    // Список дат создания заметок
    private var dates: Array<String> = Array<String>(1){""}

    init {
        this.names = names
        this.descriptions = descriptions
        this.dates = dates
    }

    fun getName(index: Int): String {
        return names[index]
    }

    fun getDescription(index: Int): String {
        return descriptions[index]
    }

    fun getDate(index: Int): String {
        return dates[index]
    }

    fun getSize(): Int {
        return names.size
    }

    fun setName(index: Int, name: String) {
        names[index] = name
    }

    fun setDescription(index: Int, description: String) {
        descriptions[index] = description
    }

    fun setDate(index: Int, date: String) {
        dates[index] = date
    }

    public fun addNote(index : Int, name : String, description : String, date : String) {
        var newArraySize : Int = this.names.size + 1
        if (index < newArraySize) {
            var newNames : Array<String> = Array<String>(newArraySize){""}
            var newDescriptions : Array<String> = Array<String>(newArraySize){""}
            var newDates : Array<String> = Array<String>(newArraySize){""}

            var counter : Int = 0
            for (i in 0..newArraySize - 1) {
                if (counter == index) {
                    newNames[i] = name
                    newDescriptions[i] = description
                    newDates[i] = date
                } else {
                    newNames[i] = names[counter]
                    newDescriptions[i] = descriptions[counter]
                    newDates[i] = dates[counter]
                    counter++
                }
            }
        }
    }
}