package com.example.tripreminder.RoomDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "trips")
public class TripTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(defaultValue = "Trip Tile",name = "title")
    private String title;
    private String time;
    private String date;
    private String status;
    private String repetition;
    private boolean ways;
    private String from;
    private String to;
    private String notes;
//MNN
    public TripTable(String title, String time, String date, String status, String repetition, boolean ways, String from, String to, String notes) {
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getRepetition() {
        return repetition;
    }

    public boolean getWays() {
        return ways;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getNotes() {
        return notes;
    }

    public int getId() {
        return id;
    }
}
