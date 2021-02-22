package com.example.MyBleeds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ChildList extends ArrayAdapter<Patient> {
    private Activity context;
    private List<Patient> patients;



    public ChildList(Activity context, List<Patient> patients){
        super(context, R.layout.layout_childlist, patients);
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    //code for listview that displays bleeds
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_childlist,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textChildName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textChildDOB);

        Patient patient = patients.get(position);

        textViewName.setText(patient.getPatientName());
        textViewDate.setText(String.valueOf(patient.getPatientDOB()));


        return listViewItem;


    }




}
