package com.example.MyBleeds;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerPatientAdapter extends RecyclerView.Adapter<RecyclerPatientAdapter.ViewHolder> {

    //most code taken from tutorial
    //https://www.youtube.com/watch?v=BrDX6VTgTkg
    private static final String tag  = "RecyclerView";
    private Context mContext;
    private ArrayList<Patient> patientArrayList;

    public static final String PATIENT_ID = "PatientID";

    DatabaseReference dataRef;

    public RecyclerPatientAdapter(Context mContext, ArrayList<Patient> patientArrayList) {
        this.mContext = mContext;
        this.patientArrayList = patientArrayList;
    }

    @NonNull
    @Override
    public RecyclerPatientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //gets our layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Context fcontext ;

        holder.TextViewNameList.setText(patientArrayList.get(position).getPatientName());
        holder.TextViewSeverity.setText(patientArrayList.get(position).getPatientSeverity());
        holder.TextViewRegion.setText(patientArrayList.get(position).getPatientRegion());
        holder.TextViewDOB2.setText(patientArrayList.get(position).getPatientDOB());

        //Glide image
        Glide.with(mContext).load(patientArrayList.get(position).getImageURL()).into(holder.imageView);

    }
    @Override
    public int getItemCount() {
        return patientArrayList.size();
    }

//changes recycler view depending on whats being typed.
    public void filterlist(ArrayList<Patient> filteredList){
        patientArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //The widgets
        ImageView imageView;
        TextView TextViewNameList,TextViewSeverity,TextViewRegion,TextViewDOB2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //From patient_item.xml , initating the view
            TextViewNameList = itemView.findViewById(R.id.TextViewNameList);
            TextViewDOB2 = itemView.findViewById(R.id.TextViewDOB2);
            TextViewSeverity = itemView.findViewById(R.id.TextViewSeverity);
            TextViewRegion = itemView.findViewById(R.id.TextViewRegion);



        }
    }


    }


