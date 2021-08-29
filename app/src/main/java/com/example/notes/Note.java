package com.example.notes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note {
    private String title;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    private String context;
    private String date;
    private String noteId;

    public Note(String title, String context) {
        this.title = title;
        this.context = context;
        this.date =  new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public Note(String title, String context,Date date,String noteId) {
        this.title = title;
        this.context = context;
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.noteId = noteId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Note(){

    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
