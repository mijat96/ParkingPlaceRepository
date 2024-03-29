package com.rmj.parking_place.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.rmj.parking_place.model.ParkingPlace;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "notification")
public class NotificationDb {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private long id;
    private String title;
    private String text;
    private String type;
    private long dateTime;
    private long parkingPlaceId;

    public NotificationDb() {

    }

    public NotificationDb(long id, String title, String text, String type, long dateTime, long parkingPlaceId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.type = type;
        this.dateTime = dateTime;
        this.parkingPlaceId = parkingPlaceId;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public void setParkingPlaceId(long parkingPlaceId) { this.parkingPlaceId = parkingPlaceId; }

    public long getParkingPlaceId() { return parkingPlaceId; }
}
