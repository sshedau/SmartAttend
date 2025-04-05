package com.example.smartattend;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewAttendanceFacultyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AttendanceFacultyAdapter adapter;
    DatabaseHelper dbHelper;
    List<AttendanceModel> attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_faculty);

        recyclerView = findViewById(R.id.recyclerViewAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        attendanceList = dbHelper.getAllAttendance();

        if (attendanceList.isEmpty()) {
            Toast.makeText(this, "No attendance records found", Toast.LENGTH_SHORT).show();
        }

        adapter = new AttendanceFacultyAdapter(attendanceList);
        recyclerView.setAdapter(adapter);
    }
}
