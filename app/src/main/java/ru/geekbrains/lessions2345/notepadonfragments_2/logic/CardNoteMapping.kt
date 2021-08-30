package ru.geekbrains.lessions2345.notepadonfragments_2.logic

class CardNoteMapping {
    object Fields {
        const val NAME : String = "name"
        const val DESCRIPTION : String = "description"
        const val TEXT : String = "text"
        const val YEAR : String = "year"
        const val MONTH : String = "month"
        const val DAY : String = "day"

        @JvmStatic
        fun toCardNote(id : String, doc : HashMap<String, Any>) : CardNote {
           var answer : CardNote = CardNote(
                doc.get(Fields.NAME) as String,
                doc.get(Fields.DESCRIPTION) as String,
                doc.get(Fields.TEXT) as String,
                Math.toIntExact(doc.get(Fields.YEAR) as Long),
                Math.toIntExact(doc.get(Fields.MONTH) as Long),
                Math.toIntExact(doc.get(Fields.DAY) as Long))
            answer.setId(id)
            return answer
        }

        @JvmStatic
        fun toDocument(cardData : CardNote) : Map<String, Any> {
            var answer : HashMap<String, Any> = HashMap()
            answer.put(Fields.NAME, cardData.getName())
            answer.put(Fields.DESCRIPTION, cardData.getDescription())
            answer.put(Fields.TEXT, cardData.getText())
            answer.put(Fields.YEAR, cardData.getDateYear())
            answer.put(Fields.MONTH, cardData.getDateMonth())
            answer.put(Fields.DAY, cardData.getDateDay())
            return answer
        }
    }
}