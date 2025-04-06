package com.example.smartattend;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentHomeActivity extends AppCompatActivity {

    TextView teachName, studEmail;
    CardView viewAttendanceCard;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        // Initialize views
        teachName = findViewById(R.id.teach_name);
        studEmail = findViewById(R.id.stud_email);
        viewAttendanceCard = findViewById(R.id.viewAttendance);
        db = new DatabaseHelper(this);

        // Receive email from MainActivity
        String email = getIntent().getStringExtra("email");

        if (email != null) {
            studEmail.setText(email);

            Cursor cursor = db.getStudentByEmail(email);
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex("name")); // update with actual column name
                teachName.setText(name);
            } else {
                teachName.setText("Student");
            }
        } else {
            Toast.makeText(this, "Error: Email not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Handle View Attendance click
        viewAttendanceCard.setOnClickListener(view -> {
            Intent intent = new Intent(StudentHomeActivity.this, ViewAttendanceStudentActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }
}
