package com.example.smartattend;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AttendanceFragment extends Fragment {
    EditText editTextRollNo;
    Button buttonMarkAttendance, buttonViewAttendance;
    DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        editTextRollNo = view.findViewById(R.id.editTextRollNo);
        buttonMarkAttendance = view.findViewById(R.id.buttonMarkAttendance);
        buttonViewAttendance = view.findViewById(R.id.buttonViewAttendance);

        databaseHelper = new DatabaseHelper(getContext());

        DatePicker datePicker = view.findViewById(R.id.datePicker);

        buttonMarkAttendance.setOnClickListener(v -> {
            String studentRollNo = editTextRollNo.getText().toString().trim();
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month starts from 0
            int year = datePicker.getYear();

            String selectedDate = day + "/" + month + "/" + year;

            markAttendance(studentRollNo, selectedDate, "Present"); // Mark attendance
        });

        buttonViewAttendance.setOnClickListener(v -> showAttendanceRecords());

        return view;
    }

    public void markAttendance(String rollNo, String date, String status) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext()); // For Fragments
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("roll_no", rollNo);
        values.put("date", date);
        values.put("status", status);

        db.insert("attendance", null, values);
        db.close();
    }

    private void showAttendanceRecords() {
        Cursor cursor = databaseHelper.getAttendance();
        if (cursor.getCount() == 0) {
            showMessage("Attendance Records", "No attendance records found.");
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (cursor.moveToNext()) {
            buffer.append("Roll No: ").append(cursor.getString(0)).append("\n");
            buffer.append("Total Days Present: ").append(cursor.getInt(1)).append("\n\n");
        }

        showMessage("Attendance Records", buffer.toString());
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public List<String> getAttendanceByDate(String date) {
        List<String> attendanceList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext()); // For Fragments
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT roll_no, status FROM attendance WHERE date=?", new String[]{date});

        while (cursor.moveToNext()) {
            String rollNo = cursor.getString(0);
            String status = cursor.getString(1);
            attendanceList.add(rollNo + " - " + status);
        }
        cursor.close();
        db.close();

        return attendanceList;
    }

}
