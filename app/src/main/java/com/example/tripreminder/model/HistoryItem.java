package com.example.tripreminder.model;

public class HistoryItem {

    private String title;
    private String time;
    private String date;
    private String status;
    private String from;
    private String to;

    public HistoryItem(String title, String time, String date, String status, String from, String to) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.status = status;
        this.from = from;
        this.to = to;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
