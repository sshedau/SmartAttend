package com.example.smartattend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

public class FacultyHomeActivity extends AppCompatActivity {

    TextView teachName, teachClass, teachEmail;
    CardView cdTakeAttendance, cdViewAttendance, cdAddStudent, cdAnalyseAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home); // Update with your XML filename

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home (Faculty)");

        // Initialize Views
        teachName = findViewById(R.id.teach_name);
        teachClass = findViewById(R.id.teach_class);
        teachEmail = findViewById(R.id.teach_email);

        cdTakeAttendance = findViewById(R.id.cdTA);
        cdViewAttendance = findViewById(R.id.cdVA);
        cdAddStudent = findViewById(R.id.cdAS);
        cdAnalyseAttendance = findViewById(R.id.cdEA);

        // Set dummy values - you can fetch from SharedPreferences or database
        teachName.setText("Mr. Rahul Sharma");
        teachClass.setText("Class: CSE-3A");
        teachEmail.setText("rahul.sharma@college.edu");

        // Click Listeners for CardViews
        cdTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Take Attendance activity
                Intent intent = new Intent(FacultyHomeActivity.this, TakeAttendanceActivity.class);
                startActivity(intent);
            }
        });

        cdViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to View Attendance activity
                Intent intent = new Intent(FacultyHomeActivity.this, ViewAttendanceFacultyActivity.class);
                startActivity(intent);
            }
        });


//        cdAddStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Navigate to Add Student activity
//                Intent intent = new Intent(FacultyDashboardActivity.this, AddStudentActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        cdAnalyseAttendance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Navigate to Analyse Attendance activity
//                Intent intent = new Intent(FacultyDashboardActivity.this, AnalyseAttendanceActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}