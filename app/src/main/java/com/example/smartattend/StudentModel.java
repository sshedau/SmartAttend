package com.example.smartattend;

public class StudentModel {
    private String name;
    private String roll; // <-- Add this
    private boolean isPresent;

    public StudentModel(String name, String roll, boolean isPresent) {
        this.name = name;
        this.roll = roll;
        this.isPresent = isPresent;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
