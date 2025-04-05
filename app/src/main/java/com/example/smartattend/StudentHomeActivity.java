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
    DatabaseHelper db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home); // Make sure this matches your XML filename

        // Initialize views
        teachName = findViewById(R.id.teach_name);
        studEmail = findViewById(R.id.stud_email);
        viewAttendanceCard = findViewById(R.id.viewAttendance);
        db = new DatabaseHelper(this) ;

        // Receive data from LoginActivity (or wherever you sent it from)
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        // Set values
        teachName.setText(username != null ? username : "Student");
        studEmail.setText(email != null ? email : "student@email.com");

        if (username != null) {
            Cursor studentCursor = db.getStudentByUsername(username);
            // proceed with cursor data (e.g., show welcome message, fetch info, etc.)
        }
        else {
            Toast.makeText(this, "Error: Username not found!", Toast.LENGTH_SHORT).show();
            finish(); // Optionally close the activity if username is essential
        }

        // Handle View Attendance click
        viewAttendanceCard.setOnClickListener(view -> {
            Intent intent = new Intent(StudentHomeActivity.this, ViewAttendanceStudentActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            startActivity(intent);
        });

    }
}
