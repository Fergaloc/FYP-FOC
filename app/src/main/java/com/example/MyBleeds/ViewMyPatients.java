package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewMyPatients extends AppCompatActivity {
    private View PatientView;
    private RecyclerView myPatientList;

    private DatabaseReference  PatientsRef, bleedsRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);


        myPatientList = (RecyclerView) findViewById(R.id.recyclerView);
        myPatientList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        PatientsRef = FirebaseDatabase.getInstance().getReference().child("patients").child(currentUserID);
        bleedsRef = FirebaseDatabase.getInstance().getReference().child("bleeds");

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Patient>().setQuery(PatientsRef, Patient.class).build();

        FirebaseRecyclerAdapter<Patient, ViewMyPatients.PatientsViewHolder> adapter = new FirebaseRecyclerAdapter<Patient, ViewMyPatients.PatientsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final PatientsViewHolder holder, int position, @NonNull Patient model) {

                final String patientID = getRef(position).getKey();
                PatientsRef.child(patientID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String userName = dataSnapshot.child("patientName").getValue().toString();
                        String userRegion = dataSnapshot.child("patientRegion").getValue().toString();
                        String userDOB = dataSnapshot.child("patientDOB").getValue().toString();
                        String userSeverity = dataSnapshot.child("patientSeverity").getValue().toString();

                        holder.PatientName.setText(userName);
                        holder.patientDOB.setText(userDOB);
                        holder.patientRegion.setText(userRegion);
                        holder.patientSeverity.setText(userSeverity);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent patientIntent = new Intent(getApplicationContext(), ViewPatientBleeds.class);
                                patientIntent.putExtra("patientID", patientID);
                                startActivity(patientIntent);

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item,parent,false);
              PatientsViewHolder viewHolder = new PatientsViewHolder(view);
                return  viewHolder;

            }
        };

        myPatientList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PatientsViewHolder extends RecyclerView.ViewHolder{

        TextView PatientName, patientSeverity, patientRegion, patientDOB ;



        public PatientsViewHolder(@NonNull View itemView) {
            super(itemView);

            PatientName = itemView.findViewById(R.id.TextViewNameList);
            patientSeverity = itemView.findViewById(R.id.TextViewSeverity);
            patientRegion  = itemView.findViewById(R.id.TextViewRegion);
            patientDOB = itemView.findViewById(R.id.TextViewDOB2);





        }
    }



}