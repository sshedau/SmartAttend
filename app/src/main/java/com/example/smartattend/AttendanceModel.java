package com.example.smartattend;

public class AttendanceModel {
    private String roll;
    private String date;
    private String status;

    public AttendanceModel(String roll, String date, String status) {
        this.roll = roll;
        this.date = date;
        this.status = status;
    }

    public String getRoll() {
        return roll;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
