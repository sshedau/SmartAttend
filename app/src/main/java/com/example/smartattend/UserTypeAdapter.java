package com.example.smartattend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class UserTypeAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] userTypes;

    public UserTypeAdapter(@NonNull Context context, String[] userTypes) {
        super(context, R.layout.spinner_item, userTypes);
        this.context = context;
        this.userTypes = userTypes;
    }

    @Override
    public boolean isEnabled(int position) {
        // Make the first item (Select User Type) non-selectable
        return position != 0;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;

        if (position == 0) {
            tv.setTextColor(ContextCompat.getColor(context, R.color.inputHint)); // Hint color
        }
        else {
            tv.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
        }

        return view;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView) view;

        if (position == 0) {
            tv.setTextColor(ContextCompat.getColor(context, R.color.inputHint)); // Hint color
        }
        else {
            tv.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
        }

        return view;
    }
}
