package ru.geekbrains.lessions2345.notepadonfragments.logic;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class CardNoteMapping {

    public static class Fields{
        public final static String NAME = "date";
        public final static String DESCRIPTION = "description";
        public final static String TEXT = "text";
        public final static String YEAR = "year";
        public final static String MONTH = "month";
        public final static String DAY = "day";
    }

    public static CardNote toCardNote(String id, Map<String, Object> doc) {
        CardNote answer = new CardNote(
                (String) doc.get(Fields.NAME),
                (String) doc.get(Fields.DESCRIPTION),
                (String) doc.get(Fields.TEXT),
                (int) doc.get(Fields.YEAR),
                (int) doc.get(Fields.MONTH),
                (int) doc.get(Fields.DAY));
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(CardNote cardData){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.NAME, cardData.getName());
        answer.put(Fields.DESCRIPTION, cardData.getDescription());
        answer.put(Fields.TEXT, cardData.getText());
        answer.put(Fields.YEAR, cardData.getDateYear());
        answer.put(Fields.MONTH, cardData.getDateMonth());
        answer.put(Fields.DAY, cardData.getDateDay());
        return answer;
    }
}
