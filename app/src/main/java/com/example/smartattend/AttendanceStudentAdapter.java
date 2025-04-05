package com.example.smartattend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceStudentAdapter extends RecyclerView.Adapter<AttendanceStudentAdapter.AttendanceViewHolder> {

    private List<AttendanceModel> attendanceList;

    public AttendanceStudentAdapter(List<AttendanceModel> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_student_item, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceModel model = attendanceList.get(position);
        holder.date.setText("Date: " + model.getDate());
        holder.status.setText("Status: " + model.getStatus());
    }


    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, status;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_attendance_date);
            status = itemView.findViewById(R.id.tv_attendance_status);
        }
    }
}