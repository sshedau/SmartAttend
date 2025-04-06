package com.example.smartattend;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewAttendanceStudentActivity extends AppCompatActivity {

    ListView attendanceListView;
    DatabaseHelper db;
    String username;
    ArrayList<HashMap<String, String>> attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_student);

        attendanceListView = findViewById(R.id.attendanceListView);
        db = new DatabaseHelper(this);


        attendanceList = new ArrayList<>();
        // Receiving email from intent
        String email = getIntent().getStringExtra("email");

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Email not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

// Fetching RollNumber using Email
        String roll = null;
        Cursor studentCursor = db.getStudentByEmail(email);
        if (studentCursor != null && studentCursor.moveToFirst()) {
            roll = studentCursor.getString(studentCursor.getColumnIndexOrThrow("roll"));
            studentCursor.close();
        }

        if (roll == null) {
            Toast.makeText(this, "Roll number not found for email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

// Fetching attendance using roll number
        attendanceList = new ArrayList<>();
        Cursor cursor = db.getAttendanceForStudent(roll);



        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.d("AttendanceDebug", "Cursor count: " + cursor.getCount());
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                HashMap<String, String> record = new HashMap<>();
                record.put("date", "Date: " + date);
                record.put("status", "Status: " + status);

                attendanceList.add(record);
            } while (cursor.moveToNext());

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    attendanceList,
                    android.R.layout.simple_list_item_2,
                    new String[]{"date", "status"},
                    new int[]{android.R.id.text1, android.R.id.text2}
            );


            attendanceListView.setAdapter(adapter);

        } else {
            Toast.makeText(this, "No attendance data found", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}
