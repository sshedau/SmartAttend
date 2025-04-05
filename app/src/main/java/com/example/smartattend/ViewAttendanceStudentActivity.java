package com.example.smartattend;

import android.database.Cursor;
import android.os.Bundle;
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

        username = getIntent().getStringExtra("username");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        attendanceList = new ArrayList<>();
        Cursor cursor = db.getAttendanceForStudent(username);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                HashMap<String, String> record = new HashMap<>();
                record.put("subject", "Subject: " + subject);
                record.put("date", "Date: " + date);
                record.put("status", "Status: " + status);

                attendanceList.add(record);
            } while (cursor.moveToNext());

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    attendanceList,
                    android.R.layout.simple_list_item_2,
                    new String[]{"subject", "status"},
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
