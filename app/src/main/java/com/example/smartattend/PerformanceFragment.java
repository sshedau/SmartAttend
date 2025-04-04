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

public class PerformanceFragment extends Fragment {
    EditText editTextRollNo, editTextTestScore;
    Button buttonSubmitScore, buttonViewPerformance;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        editTextRollNo = view.findViewById(R.id.editTextRollNo);
        editTextTestScore = view.findViewById(R.id.editTextTestScore);
        buttonSubmitScore = view.findViewById(R.id.buttonSubmitScore);
        buttonViewPerformance = view.findViewById(R.id.buttonViewPerformance);

        databaseHelper = new DatabaseHelper(getContext());

        buttonSubmitScore.setOnClickListener(v -> submitScore());
        buttonViewPerformance.setOnClickListener(v -> showPerformanceRecords());

        return view;
    }

    private void submitScore() {
        String rollNo = editTextRollNo.getText().toString().trim();
        String testScore = editTextTestScore.getText().toString().trim();

        if (rollNo.isEmpty() || testScore.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = databaseHelper.insertTestScore(rollNo, Integer.parseInt(testScore));
        if (inserted) {
            Toast.makeText(getContext(), "Score Submitted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to submit score", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPerformanceRecords() {
        Cursor cursor = databaseHelper.getPerformance();
        if (cursor.getCount() == 0) {
            showMessage("Performance Records", "No performance records found.");
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (cursor.moveToNext()) {
            buffer.append("Roll No: ").append(cursor.getString(0)).append("\n");
            buffer.append("Average Score: ").append(cursor.getDouble(1)).append("\n\n");
        }

        showMessage("Performance Records", buffer.toString());
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
