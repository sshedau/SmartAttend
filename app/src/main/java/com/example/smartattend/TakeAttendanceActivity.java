package com.example.smartattend;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TakeAttendanceActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private Button btnPickDate, btnSubmitAttendance;
    private RecyclerView recyclerViewStudents;

    private StudentAdapter studentAdapter;
    private List<StudentModel> studentList;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        //
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        studentList = dbHelper.getAllStudents(); // ✅ fetch real students
        studentAdapter = new StudentAdapter(studentList);


        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar_take_attendance);
        setSupportActionBar(toolbar);

        tvSelectedDate = findViewById(R.id.tv_selected_date);
        btnPickDate = findViewById(R.id.btn_pick_date);
        btnSubmitAttendance = findViewById(R.id.btn_submit_attendance);
        recyclerViewStudents = findViewById(R.id.recyclerView_students);

        // Initialize RecyclerView
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
       // Replace with DB data;
        recyclerViewStudents.setAdapter(studentAdapter);

        // Pick Date
        btnPickDate.setOnClickListener(v -> openDatePicker());

        // Submit Attendance
        btnSubmitAttendance.setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            }
            else {
                List<StudentModel> presentStudents = studentAdapter.getPresentStudents();
                DatabaseHelper db_Helper = new DatabaseHelper(TakeAttendanceActivity.this);

                for (StudentModel student : studentList) {
                    String status = student.isPresent() ? "Present" : "Absent";
                    db_Helper.markAttendance(student.getRoll(), selectedDate, status);
                }

                Toast.makeText(this, "Attendance submitted!", Toast.LENGTH_SHORT).show();
                finish(); // or redirect to dashboard
                // Save to SQLite here if needed
            }
        });

    }

    private void openDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    month += 1;
                    selectedDate = dayOfMonth + "/" + month + "/" + year;
                    tvSelectedDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }




}

