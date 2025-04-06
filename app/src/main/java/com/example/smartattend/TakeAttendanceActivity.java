package com.example.smartattend;

import static com.example.smartattend.DatabaseHelper.TABLE_ATTENDANCE;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        requestNotificationPermission();
        // Create Notification Channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Attendance Notifications";
            String description = "Notifications about attendance status";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("attendance_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
            } else {
                List<StudentModel> presentStudents = studentAdapter.getPresentStudents();
                DatabaseHelper db_Helper = new DatabaseHelper(TakeAttendanceActivity.this);

                boolean anyMarked = false;

                for (StudentModel student : studentList) {
                    String status = student.isPresent() ? "Present" : "Absent";

                    try {
                        boolean success = db_Helper.markAttendance(student.getRoll(), selectedDate, status);

                        if (success) {
                            db_Helper.updateAttendanceNotificationStatus(student.getRoll(), selectedDate);
                            anyMarked = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error marking attendance for roll " + student.getRoll(), Toast.LENGTH_SHORT).show();
                    }
                }

                if (anyMarked) {
                    sendFacultyNotification("Attendance has been successfully submitted for " + selectedDate);
                    Toast.makeText(this, "Attendance submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Attendance was already marked for all students.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void openDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvSelectedDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /*
    // Method to send the notification
    private void sendAttendanceNotification(StudentModel student, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "attendance_channel")
                .setSmallIcon(R.drawable.ic_notification) // Your notification icon
                .setContentTitle("Attendance Status")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Notification ID can be unique for each student (for example, using their roll number)
        int notificationId = Integer.parseInt(student.getRoll());; // Unique ID for each student
        notificationManager.notify(notificationId, builder.build());
    }
    */
    private void sendFacultyNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "attendance_channel")
                .setSmallIcon(R.drawable.ic_notification) // Replace with your faculty notification icon
                .setContentTitle("Attendance Submitted")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = (int) System.currentTimeMillis(); // Unique ID
        notificationManager.notify(notificationId, builder.build());
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }


}


