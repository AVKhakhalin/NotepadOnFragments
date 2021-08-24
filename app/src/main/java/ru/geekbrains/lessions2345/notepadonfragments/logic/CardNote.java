package ru.geekbrains.lessions2345.notepadonfragments.logic;

import android.os.Parcel;
import android.os.Parcelable;

public class CardNote implements Parcelable {

    // Id заметки
    private String id;
    // Имя заметки
    private String name;
    // Краткое описание заметки
    private String description;
    // Текст заметки
    private String text;
    // Год создания заметки
    private int dateYear;
    // Месяц создания заметки
    private int dateMonth;
    // День создания заметки
    private int dateDay;

    public CardNote(String name, String description, String text, int dateYear, int dateMonth, int dateDay) {
        this.name = name;
        this.description = description;
        this.text = text;
        this.dateYear = dateYear;
        this.dateMonth = dateMonth;
        this.dateDay = dateDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return String.format("%s.%s.%d", (dateDay < 10 ? "0" : "") + String.valueOf(dateDay), (dateMonth < 10 ? "0" : "") + String.valueOf(dateMonth), dateYear);
    }

    protected CardNote(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        text = in.readString();
        dateYear = in.readInt();
        dateMonth = in.readInt();
        dateDay = in.readInt();
    }

    public static final Creator<CardNote> CREATOR = new Creator<CardNote>() {
        @Override
        public CardNote createFromParcel(Parcel in) {
            return new CardNote(in);
        }

        @Override
        public CardNote[] newArray(int size) {
            return new CardNote[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(text);
        dest.writeInt(dateYear);
        dest.writeInt(dateMonth);
        dest.writeInt(dateDay);
    }
}
