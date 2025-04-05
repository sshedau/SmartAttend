package com.example.smartattend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final List<StudentModel> studentList;

    public StudentAdapter(List<StudentModel> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_attendance, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentModel student = studentList.get(position);
        holder.tvStudentName.setText(student.getName());
        holder.checkBoxPresent.setChecked(student.isPresent());

        holder.checkBoxPresent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setPresent(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public List<StudentModel> getPresentStudents() {
        List<StudentModel> presentList = new ArrayList<>();
        for (StudentModel student : studentList) {
            if (student.isPresent()) {
                presentList.add(student);
            }
        }
        return presentList;
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        CheckBox checkBoxPresent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            checkBoxPresent = itemView.findViewById(R.id.checkbox_present);
        }
    }
}
