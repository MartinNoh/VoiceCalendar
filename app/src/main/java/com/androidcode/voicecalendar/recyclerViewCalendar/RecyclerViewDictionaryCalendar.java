package com.androidcode.voicecalendar.recyclerViewCalendar;

public class RecyclerViewDictionaryCalendar {
    private int id;
    private String date;
    private String content;

    public RecyclerViewDictionaryCalendar(int id, String date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
