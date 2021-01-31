package com.example.tripreminder.model;


public class Trip {
    private int id;
    private String title;
    private String time;
    private String date;
    private boolean status;
    private String repetition;
    private boolean ways;
    private String from;
    private String to;
    private String notes;

    public Trip(int id, String title, String time, String date, boolean status, String repetition, boolean ways, String from, String to, String notes) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.date = date;
        this.status = status;
        this.repetition = repetition;
        this.ways = ways;
        this.from = from;
        this.to = to;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public boolean getWays() {
        return ways;
    }

    public void setWays(boolean ways) {
        this.ways = ways;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
