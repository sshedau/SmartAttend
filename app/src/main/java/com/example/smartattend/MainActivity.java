package com.example.smartattend;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure your layout contains spinner with correct ID

        spinner = findViewById(R.id.spinner_user_type);

        String[] userTypes = {"Select User Type", "Student", "Faculty"};

        UserTypeAdapter adapter = new UserTypeAdapter(this, userTypes);
        spinner.setAdapter(adapter);

        spinner.setSelection(0); // Default selection

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selected = parent.getItemAtPosition(position).toString();
                    Toast.makeText(MainActivity.this, "Selected: " + selected, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        String selected = spinner.getSelectedItem().toString();
        if (selected.equals("Select User Type")) {
            Toast.makeText(this, "Please select a valid user type", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
