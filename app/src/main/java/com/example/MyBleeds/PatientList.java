package com.example.MyBleeds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;



public class PatientList extends ArrayAdapter<Patient> {

    private Activity context;
    private List<Patient> patientList;

    public PatientList(Activity context, List<Patient> patientList){
        super(context, R.layout.list_layout, patientList);
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Patient patient = patientList.get(position);

        textViewName.setText(patient.getPatientName());
        textViewGenre.setText(patient.getPatientRegion());

        return listViewItem;

    }
}
