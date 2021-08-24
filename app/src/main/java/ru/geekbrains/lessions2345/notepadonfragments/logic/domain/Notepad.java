package ru.geekbrains.lessions2345.notepadonfragments.logic.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import ru.geekbrains.lessions2345.notepadonfragments.model.CONSTANTS;
import ru.geekbrains.lessions2345.notepadonfragments.ui.MainActivity;

public class Notepad implements Parcelable {
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
    private final int START_SIZE_INTARRAYS = CONSTANTS.DELTA_CHANGE_INT_ARRAYS;

    // Список имен заметок
    private ArrayList<String> name;
    // Список кратких описаний заметок
    private ArrayList<String> description;
    // Список текстов заметок
    private ArrayList<String> text;
    // Массив годов создания заметок
    private int[] dateYear;
    // Массив месяцев создания заметок
    private int[] dateMonth;
    // Массив дней создания заметок
    private int[] dateDay;

    public Notepad() {
        name = new ArrayList<String>();
        name.add(CONSTANTS.NAME_EMPTY_NOTE_ADD);
        description = new ArrayList<String>();
        description.add(CONSTANTS.NAME_EMPTY_NOTE_ADD);
        text = new ArrayList<String>();
        text.add("");
        dateYear = new int[START_SIZE_INTARRAYS];
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        dateYear[0] = calendar.get(Calendar.YEAR);
        dateMonth = new int[START_SIZE_INTARRAYS];
        dateMonth[0] = calendar.get(Calendar.MONTH) + 1;
        dateDay = new int[START_SIZE_INTARRAYS];
        dateDay[0] = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // Вывести список всех дат записей, без учёта нулевого элемента
    public String getAllDates() {
        String result = "";
        for (int i = 1; i <= getNumberElements(); i++) {
            result += String.format("%s.%s.%d\n", (dateDay[i] < 10 ? "0" : "") + String.valueOf(dateDay[i]), (dateMonth[i] < 10 ? "0" : "") + String.valueOf(dateMonth[i]), dateYear[i]);
        }
        return result;
    }

    // Вывести список всех имен записей, без учёта нулевого элемента
    public String getAllNames() {
        String result = "";
        for (int i = 1; i <= getNumberElements(); i++) {
            result += name.get(i) + "\n";
        }
        return result;
    }

    // Вывести количество элементов без учёта нулевого элемента, всегда присутствующего по-умолчанию
    public int getNumberElements() {
        return name.size() - 1;
    }

    // Добавить новый элемент
    public void add(String newName, String newDescription) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int numberCurElement = name.size();
        if (newName.equals("")) {
            name.add(CONSTANTS.NAME_EMPTY_NOTE);
        } else {
            name.add(newName);
        }
        if (newDescription.equals("")) {
            if (newName.equals("")) {
                description.add(CONSTANTS.DESCRIPTION_EMPTY_NOTE);
            } else {
                description.add(newName);
            }
        } else {
            description.add(newDescription);
        }
        text.add("");
        // Проверка на превышение размера списков над размерами массивов
        if (numberCurElement < dateYear.length) {
            dateYear[numberCurElement] = calendar.get(Calendar.YEAR);
            dateMonth[numberCurElement] = calendar.get(Calendar.MONTH) + 1;
            dateDay[numberCurElement] = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            int[] tempArrayYear = new int[dateYear.length];
            int[] tempArrayMonth = new int[dateYear.length];
            int[] tempArrayDay = new int[dateYear.length];
            for (int i = 0; i < dateYear.length; i++) {
                tempArrayYear[i] = dateYear[i];
                tempArrayMonth[i] = dateMonth[i];
                tempArrayDay[i] = dateDay[i];
            }
            dateYear = new int[numberCurElement + CONSTANTS.DELTA_CHANGE_INT_ARRAYS];
            dateMonth = new int[numberCurElement + CONSTANTS.DELTA_CHANGE_INT_ARRAYS];
            dateDay = new int[numberCurElement + CONSTANTS.DELTA_CHANGE_INT_ARRAYS];
            for (int i = 0; i < tempArrayYear.length; i++) {
                dateYear[i] = tempArrayYear[i];
                dateMonth[i] = tempArrayMonth[i];
                dateDay[i] = tempArrayDay[i];
            }
            dateYear[numberCurElement] = calendar.get(Calendar.YEAR);
            dateMonth[numberCurElement] = calendar.get(Calendar.MONTH) + 1;
            dateDay[numberCurElement] = calendar.get(Calendar.DAY_OF_MONTH);
        }
        // Ранжирование заметок: последняя заметка становится первой, а первая заметка - последней
        String firstName = name.get(getNumberElements());
        String firstDescription = description.get(getNumberElements());
        String firstText = text.get(getNumberElements());
        int firstDateYear = dateYear[getNumberElements()];
        int firstDateMonth = dateMonth[getNumberElements()];
        int firstDateDay = dateDay[getNumberElements()];
        for (int i = getNumberElements() - 1; i > 0; i--) {
            name.set(i + 1, name.get(i));
            description.set(i + 1, description.get(i));
            text.set(i + 1, text.get(i));
            dateYear[i + 1] = dateYear[i];
            dateMonth[i + 1] = dateMonth[i];
            dateDay[i + 1] = dateDay[i];
        }
        name.set(1, firstName);
        description.set(1, firstDescription);
        text.set(1, firstText);
        dateYear[1] = firstDateYear;
        dateMonth[1] = firstDateMonth;
        dateDay[1] = firstDateDay;
    }

    // Удалить запись
    public void remove(int index) {
        if (index > 0) {
            name.remove(index);
            description.remove(index);
            text.remove(index);

            int[] tempArrayYear = new int[dateYear.length - 1];
            int[] tempArrayMonth = new int[dateYear.length - 1];
            int[] tempArrayDay = new int[dateYear.length - 1];

            int counter = 0;

            for (int i = 0; i < dateYear.length; i++) {
                if (i != index) {
                    tempArrayYear[counter] = dateYear[i];
                    tempArrayMonth[counter] = dateMonth[i];
                    tempArrayDay[counter] = dateDay[i];
                    counter++;
                }
            }
            dateYear = new int[tempArrayYear.length];
            dateMonth = new int[tempArrayYear.length];
            dateDay = new int[tempArrayYear.length];
            for (int i = 0; i < dateYear.length; i++) {
                dateYear[i] = tempArrayYear[i];
                dateMonth[i] = tempArrayMonth[i];
                dateDay[i] = tempArrayDay[i];
            }
        }
    }

    // Получить имя заметки
    public String getName(int index) {
        if ((index >= 0) && (index <= getNumberElements())) {
            return name.get(index);
        } else {
            return CONSTANTS.NAME_EMPTY_NOTE;
        }
    }

    // Установить имя заметки
    public void setName(int index, String newName) {
        if ((index > 0) && (index <= getNumberElements())) {
            name.set(index, newName);
        }
    }

    // Вывести список всех кратких имен записей, без учёта нулевого элемента
    public String getAllDescription() {
        String result = "";
        for (int i = 1; i <= getNumberElements(); i++) {
            result += description.get(i) + "\n";
        }
        return result;
    }

    // Вывести краткое имя записи
    public String getDescription(int index) {
        if ((index > 0) && (index <= getNumberElements())) {
            return description.get(index);
        } else {
            return CONSTANTS.DESCRIPTION_EMPTY_NOTE;
        }
    }

    // Установить краткое описание заметки
    public void setDescription(int index, String newDescription) {
        if ((index > 0) && (index <= getNumberElements())) {
            description.set(index, newDescription);
        }
    }

    // Получить текст заметки
    public String getText(int index) {
        if ((index > 0) && (index <= getNumberElements())) {
            return text.get(index);
        } else {
            return "";
        }
    }

    // Установить текст заметки
    public void setText(int index, String newText) {
        if ((index > 0) && (index <= getNumberElements())) {
            text.set(index, newText);
        }
    }

    // Получить дату заметки
    public String getDate(int index) {
        if ((index > 0) && (index <= getNumberElements())) {
            return String.format("%s.%s.%d", (dateDay[index] < 10 ? "0" : "") + String.valueOf(dateDay[index]), (dateMonth[index] < 10 ? "0" : "") + String.valueOf(dateMonth[index]), dateYear[index]);
        } else {
            return "";
        }
    }

    // Установить год заметки
    public void setDateYear(int index, int newDateYear) {
        if ((index > 0) && (index <= getNumberElements())) {
            dateYear[index] = newDateYear;
        }
    }

    // Получить год заметки
    public int getDateYear(int index) {
        if ((index > 0) && (index <= getNumberElements())) {
            return dateYear[index];
        } else {
            return -1;
        }
    }

    // Получить месяц заметки
    public int getDateMonth(int index) {
        if ((index > 0) && (index <= getNumberElements())) {
            return dateMonth[index];
        } else {
            return -1;
        }
    }

    // Установить месяц заметки
    public void setDateMonth(int index, int newDateMonth) {
        if ((index > 0) && (index <= getNumberElements())) {
            dateMonth[index] = newDateMonth;
        }
    }

    // Получить день заметки
    public int getDateDay(int index) {
        if ((index > 0) && (index <= getNumberElements())) {
            return dateDay[index];
        } else {
            return -1;
        }
    }

    // Установить день заметки
    public void setDateDay(int index, int newDateDay) {
        if ((index > 0) && (index <= getNumberElements())) {
            dateDay[index] = newDateDay;
        }
    }

    // МЕТОДЫ ДЛЯ ПЕРЕДАЧИ КЛАССА
    public static Creator<Notepad> getCREATOR() {
        return CREATOR;
    }

    // Методы для передачи класса
    protected Notepad(Parcel in) {
        name = in.createStringArrayList();
        description = in.createStringArrayList();
        text = in.createStringArrayList();
        dateYear = in.createIntArray();
        dateMonth = in.createIntArray();
        dateDay = in.createIntArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(name);
        dest.writeStringList(description);
        dest.writeStringList(text);
        dest.writeIntArray(dateYear);
        dest.writeIntArray(dateMonth);
        dest.writeIntArray(dateDay);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notepad> CREATOR = new Creator<Notepad>() {
        @Override
        public Notepad createFromParcel(Parcel in) {
            return new Notepad(in);
        }

        @Override
        public Notepad[] newArray(int size) {
            return new Notepad[size];
        }
    };
}