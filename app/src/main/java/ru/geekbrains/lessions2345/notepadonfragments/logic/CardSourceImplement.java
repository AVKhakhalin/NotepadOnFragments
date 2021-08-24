package ru.geekbrains.lessions2345.notepadonfragments.logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.TimeZone;

import ru.geekbrains.lessions2345.notepadonfragments.logic.domain.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.model.TYPES_DATA;

public class CardSourceImplement implements CardSource, Parcelable {
    private TYPES_DATA typeSourceData;
    private int typeSourceData_int;
    private Notepad notepad = new Notepad();
    private int activeNoteIndex = 0;
    private boolean deleteMode = false;
    private int oldActiveNoteIndexBeforeDelete = 0;

    private CardsSourceFirebaseImpl cardsSourceFirebase;

    public CardSourceImplement(TYPES_DATA typeSourceData) {
        this.typeSourceData = typeSourceData;
        setTypeSourceData_Int(typeSourceData);
        initNotepad();
    }

    public int getOldActiveNoteIndexBeforeDelete() {
        return oldActiveNoteIndexBeforeDelete;
    }

    public void setOldActiveNoteIndexBeforeDelete(int oldActiveNoteIndexBeforeDelete) {
        this.oldActiveNoteIndexBeforeDelete = oldActiveNoteIndexBeforeDelete;
    }

    public int getActiveNoteIndex() {
        return activeNoteIndex;
    }

    public void setActiveNoteIndex(int activeNoteIndes) {
        this.activeNoteIndex = activeNoteIndes;
    }

    private void setTypeSourceData_Int(TYPES_DATA typeSourceData) {
        switch (typeSourceData) {
            case TEST_DATA:
                typeSourceData_int = 0;
                break;
            case FILE_DATA:
                typeSourceData_int = 1;
                break;
            case FIREBASE_DATA:
                typeSourceData_int = 2;
                break;
            case DATABASE_DATA:
                typeSourceData_int = 3;
                break;
            default:
        }
    }

    private void setTypeSourceData(int typeSourceData_int) {
        switch (typeSourceData_int) {
            case 0:
                typeSourceData = TYPES_DATA.TEST_DATA;
                break;
            case 1:
                typeSourceData = TYPES_DATA.FILE_DATA;
                break;
            case 2:
                typeSourceData = TYPES_DATA.FIREBASE_DATA;
                break;
            case 3:
                typeSourceData = TYPES_DATA.DATABASE_DATA;
                break;
            default:
        }
    }

    // Метод получения количества доступных заметок
    @Override
    public int size() {
        return notepad.getNumberElements();
    }

    // Метод получения карты с заметкой
    @Override
    public CardNote getCardNote(int position) {
        return new CardNote(notepad.getName(position), notepad.getDescription(position), notepad.getText(position), notepad.getDateYear(position), notepad.getDateMonth(position), notepad.getDateDay(position));
    }

    @Override
    public ListNotes getListNotes() {
        int numberElements = notepad.getNumberElements() + 1;
        String[] name = new String[numberElements];
        String[] description = new String[numberElements];
        String[] dates = new String[numberElements];
        for (int i = 0; i <= notepad.getNumberElements(); i++) {
            name[i] = notepad.getName(i);
            description[i] = notepad.getDescription(i);
            dates[i] = notepad.getDate(i);
        }
        return new ListNotes(name, description, dates);
    }

    // Метод добавления карты с заметкой
    @Override
    public void addCardNote(CardNote cardNote) {
        notepad.add(cardNote.getName(), cardNote.getDescription());
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setText(1, cardNote.getText());
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setDateYear(1, cardNote.getDateYear());
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setDateMonth(1, cardNote.getDateMonth());
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setDateDay(1, cardNote.getDateDay());
    }

    @Override
    public void addCardNote() {
        notepad.add("", "");
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setText(1, "");
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setDateYear(1, Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR));
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setDateMonth(1, Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MONTH) + 1);
        // Добавляем в индекс 1, потому что при добавлении нового элемента он всегда в классе Notepad устанавливается с индексом 1, а остальные элементы смещаются вниз
        notepad.setDateDay(1, Calendar.getInstance(TimeZone.getDefault()).get(Calendar.DAY_OF_MONTH));
    }

    // Метод установки значений в карте с заметкой
    @Override
    public void setCardNote(int position, CardNote cardNote) {
        if (!deleteMode) {
            notepad.setId(position, cardNote.getId());
            notepad.setName(position, cardNote.getName());
            notepad.setDescription(position, cardNote.getDescription());
            notepad.setText(position, cardNote.getText());
            notepad.setDateYear(position, cardNote.getDateYear());
            notepad.setDateMonth(position, cardNote.getDateMonth());
            notepad.setDateDay(position, cardNote.getDateDay());
        }
    }

    // Метод установки названия и описания заметки
    @Override
    public void setCardNote(int position, String name, String description) {
        if (!deleteMode) {
            notepad.setName(position, name);
            notepad.setDescription(position, description);
        }
    }

    // Метод установки текста в карте с заметкой
    @Override
    public void setCardNote(int position, String text) {
        if (!deleteMode) {
            notepad.setText(position, text);
        }
    }

    // Метод установки даты в карте с заметкой
    @Override
    public void setCardNote(int position, int year, int month, int day) {
        if (!deleteMode) {
            notepad.setDateYear(position, year);
            notepad.setDateMonth(position, month);
            notepad.setDateDay(position, day);
        }
    }

    // Установка режима блокировки записи данных во время удаления заметки
    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
    }

    // Инициализация класса Notepad для тестовой работы с заметками
    private void initNotepad() {
        switch (typeSourceData) {
            case TEST_DATA:
                notepad.add("ПЕРВ.ЗАМ.", "Первая заметка");
                notepad.add("ВТОР.ЗАМ.", "Вторая заметка");
                notepad.add("ТРЕТ.ЗАМ.", "Третья заметка");
                notepad.add("ЧЕТВ.ЗАМ.", "Четвёртая заметка");
                notepad.add("ПЯТ.ЗАМ.", "Пятая заметка");
                notepad.setText(5, "Текст первой заметки");
                notepad.setText(4, "Текст второй заметки");
                notepad.setText(3, "Текст третьей заметки");
                notepad.setText(2, "Текст четвёртой заметки");
                notepad.setText(1, "Текст пятой заметки");
                break;
            case FILE_DATA:
                break;
            case FIREBASE_DATA:
                cardsSourceFirebase = new CardsSourceFirebaseImpl();
                for (int i = 0; i < cardsSourceFirebase.size(); i++) {
                    notepad.add(cardsSourceFirebase.getCardNoteFirebase(i));
                }
                break;
            case DATABASE_DATA:
                break;
            default:
                break;
        }
    }

    // Удаление карточки
    @Override
    public void removeCardNote(int position) {
        switch (typeSourceData) {
            case TEST_DATA:
                notepad.remove(position);
                break;
            case FILE_DATA:
                break;
            case FIREBASE_DATA:
                cardsSourceFirebase.deleteCardNoteFirebase(position);
                break;
            case DATABASE_DATA:
                break;
            default:
                break;
        }
    }


    // Методы для парселизации объекта
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(typeSourceData_int);
        dest.writeParcelable(notepad, flags);
    }

    protected CardSourceImplement(Parcel in) {
        typeSourceData_int = in.readInt();
        setTypeSourceData(typeSourceData_int);
        notepad = in.readParcelable(Notepad.class.getClassLoader());
    }

    public static final Creator<CardSourceImplement> CREATOR = new Creator<CardSourceImplement>() {
        @Override
        public CardSourceImplement createFromParcel(Parcel in) {
            return new CardSourceImplement(in);
        }

        @Override
        public CardSourceImplement[] newArray(int size) {
            return new CardSourceImplement[size];
        }
    };

    public CardsSourceFirebaseImpl getCardsSourceFirebase() {
        return cardsSourceFirebase;
    }
}
