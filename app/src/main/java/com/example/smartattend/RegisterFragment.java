package com.example.smartattend;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {
    EditText editTextRollNo, editTextName, editTextMarks;
    Button buttonRegister, buttonReset, buttonViewAll;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize Views
        editTextRollNo = view.findViewById(R.id.editTextRollNo);
        editTextName = view.findViewById(R.id.editTextName);
        editTextMarks = view.findViewById(R.id.editTextMarks);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        buttonReset = view.findViewById(R.id.buttonReset);
        buttonViewAll = view.findViewById(R.id.buttonViewAll);

        // Initialize Database
        databaseHelper = new DatabaseHelper(getContext());

        // Register Button Click
        buttonRegister.setOnClickListener(v -> registerStudent());

        // Reset Button Click
        buttonReset.setOnClickListener(v -> clearFields());

        // View All Button Click
        buttonViewAll.setOnClickListener(v -> showStudentRecords());

        return view;
    }

    private void registerStudent() {
        String rollNo = editTextRollNo.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String marks = editTextMarks.getText().toString().trim();

        if (rollNo.isEmpty() || name.isEmpty() || marks.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = databaseHelper.insertStudent(rollNo, name, marks);
        if (inserted) {
            Toast.makeText(getContext(), "Student Registered!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(getContext(), "Failed to Register!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextRollNo.setText("");
        editTextName.setText("");
        editTextMarks.setText("");
    }

    private void showStudentRecords() {
        Cursor cursor = databaseHelper.getAllStudents();
        if (cursor.getCount() == 0) {
            showMessage("Student Records", "No records found.");
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (cursor.moveToNext()) {
            buffer.append("Roll No: ").append(cursor.getString(0)).append("\n");
            buffer.append("Name: ").append(cursor.getString(1)).append("\n");
            buffer.append("Marks: ").append(cursor.getString(2)).append("\n\n");
        }

        showMessage("Student Records", buffer.toString());
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}