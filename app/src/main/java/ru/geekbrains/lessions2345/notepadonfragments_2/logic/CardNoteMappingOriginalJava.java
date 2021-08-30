package ru.geekbrains.lessions2345.notepadonfragments_2.logic;

import java.util.HashMap;
import java.util.Map;

public class CardNoteMappingOriginalJava {

    public static class Fields {
        public final static String NAME = "name";
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
                Math.toIntExact((Long) doc.get(Fields.YEAR)),
                Math.toIntExact((Long) doc.get(Fields.MONTH)),
                Math.toIntExact((Long) doc.get(Fields.DAY)));
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(CardNote cardData) {
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
