package com.example.MyBleeds;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class patientsFragment extends Fragment {

    private View PatientView;
    private RecyclerView myPatientList;

    private DatabaseReference  PatientsRef, bleedsRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    public patientsFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceChange){

        PatientView = inflater.inflate(R.layout.activity_view_patients,container, false);

        myPatientList = (RecyclerView) PatientView.findViewById(R.id.recyclerView);
        myPatientList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        PatientsRef = FirebaseDatabase.getInstance().getReference().child("patients").child(currentUserID);
        bleedsRef = FirebaseDatabase.getInstance().getReference().child("bleeds");



        return PatientView;
    }


    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Patient>().setQuery(PatientsRef, Patient.class).build();

        FirebaseRecyclerAdapter<Patient, PatientsViewHolder> adapter = new FirebaseRecyclerAdapter<Patient, PatientsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PatientsViewHolder holder, int position, @NonNull Patient model) {

                String patientID = getRef(position).getKey();
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
