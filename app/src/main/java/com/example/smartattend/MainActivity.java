package com.example.smartattend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Spinner userTypeSpinner;
    Button loginButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.enter_email);
        passwordEditText = findViewById(R.id.enter_password);
        userTypeSpinner = findViewById(R.id.spinner_user_type);
        loginButton = findViewById(R.id.login_button);
        db = new DatabaseHelper(this);

        // Spinner values
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Select User Type", "Faculty", "Student"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String userType = userTypeSpinner.getSelectedItem().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (userType) {
                case "Faculty":
                    if (db.validateFaculty(email, password)) {
                        Toast.makeText(this, "Faculty Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, FacultyHomeActivity.class);
                        intent.putExtra("username", email);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Invalid Faculty Credentials", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case "Student":
                    if (db.validateStudent(email, password)) {
                        Toast.makeText(this, "Student Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, StudentHomeActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Invalid Student Credentials", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    Toast.makeText(this, "Please select a user type", Toast.LENGTH_SHORT).show();
            }
        });
    }
}