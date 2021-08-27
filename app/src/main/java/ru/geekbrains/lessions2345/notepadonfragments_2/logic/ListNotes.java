package ru.geekbrains.lessions2345.notepadonfragments_2.logic;

public class ListNotes {
    // Список имён заметок
    private String[] names;
    // Список кратких описаний заметок
    private String[] descriptions;
    // Список дат создания заметок
    private String[] dates;

    public ListNotes(String[] names, String[] descriptions, String[] dates) {
        this.names = names;
        this.descriptions = descriptions;
        this.dates = dates;
    }

    public String getName(int index) {
        return names[index];
    }

    public String getDescription(int index) {
        return descriptions[index];
    }

    public String getDate(int index) {
        return dates[index];
    }

    public int getSize() {
        return names.length;
    }

    public void setName(int index, String name) {
        this.names[index] = name;
    }

    public void setDescription(int index, String description) {
        this.descriptions[index] = description;
    }

    public void setDate(int index, String date) {
        this.dates[index] = date;
    }

    public void addNote(int index, String name, String description, String date) {
        int newArraySize = name.length() + 1;
        if (index < newArraySize) {
            String[] newNames = new String[newArraySize];
            String[] newDescriptions = new String[newArraySize];
            String[] newDates = new String[newArraySize];

            int counter = 0;
            for (int i = 0; i < newArraySize; i++) {
                if (counter == index) {
                    newNames[i] = name;
                    newDescriptions[i] = description;
                    newDates[i] = date;
                } else {
                    newNames[i] = names[counter];
                    newDescriptions[i] = descriptions[counter];
                    newDates[i] = dates[counter];
                    counter++;
                }
            }
        }
    }
}
