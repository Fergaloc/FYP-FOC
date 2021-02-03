package com.example.MyBleeds;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myPatientAdapter extends RecyclerView.Adapter<myPatientAdapter.myPatientAdapterViewHolder> {

    public Context c;
    public ArrayList<Patient> arrayList;

    public myPatientAdapter(Context c, ArrayList<Patient> arrayList){

        this.c=c;
        this.arrayList=arrayList;

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public myPatientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item,parent,false);


        return new myPatientAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myPatientAdapterViewHolder holder, int position) {

        Patient  patient = arrayList.get(position);

        holder.textViewName.setText(patient.getPatientName());
        holder.textViewRegion.setText(patient.getPatientRegion());
        holder.textViewSeverity.setText(patient.getPatientSeverity());
        holder.textViewDOB.setText(patient.getPatientDOB());

    }

    //Adapter created for the search view.
    public class myPatientAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName;
        public TextView textViewRegion;
        public TextView textViewSeverity;
        public TextView textViewDOB;



        public myPatientAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.TextViewNameList);
            textViewRegion = (TextView) itemView.findViewById(R.id.TextViewRegion);
            textViewSeverity = (TextView) itemView.findViewById(R.id.TextViewSeverity);
            textViewDOB = (TextView) itemView.findViewById(R.id.TextViewDOB2);


        }
    }


}
