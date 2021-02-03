package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;


//https://www.youtube.com/watch?v=FFHuYcB3YnU
//Code to display patients from Firebase and be able to view the patients bleeds via on click listener

public class ViewMyPatients extends AppCompatActivity {
    private View PatientView;
    private RecyclerView myPatientList;

    private DatabaseReference PatientsRef, bleedsRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    EditText editTextSearch;
    DatabaseReference SearchRef;

    ArrayList<Patient> arrayList;

    myPatientAdapter adapter;

    Uri uriConvert;

    Context context;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);

        arrayList = new ArrayList<>();
        editTextSearch = (EditText) findViewById(R.id.EdiTextSearch);
        myPatientList = (RecyclerView) findViewById(R.id.recyclerView);
        myPatientList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        PatientsRef = FirebaseDatabase.getInstance().getReference().child("patients").child(currentUserID);
        bleedsRef = FirebaseDatabase.getInstance().getReference().child("bleeds");
        SearchRef = FirebaseDatabase.getInstance().getReference().child("patients").child(currentUserID);

        context = getApplicationContext();



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

                        //getting the values for the recycler view
                        //https://www.youtube.com/watch?v=FFHuYcB3YnU - some code taken from tutorial
                        final String userName = dataSnapshot.child("patientName").getValue().toString();
                        String userRegion = dataSnapshot.child("patientRegion").getValue().toString();
                        String userDOB = dataSnapshot.child("patientDOB").getValue().toString();
                        String userSeverity = dataSnapshot.child("patientSeverity").getValue().toString();
                        String userURL = dataSnapshot.child("imageURL").getValue().toString();

                        uriConvert = Uri.parse(userURL);

                        Glide.with(context)
                                .load(uriConvert)
                                .into(holder.PatientURL);

                        holder.PatientName.setText(userName);
                        holder.patientDOB.setText(userDOB);
                        holder.patientRegion.setText(userRegion);
                        holder.patientSeverity.setText(userSeverity);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Passing the user ID to the next page
                                Intent patientIntent = new Intent(getApplicationContext(), ViewPatientHome.class);
                                patientIntent.putExtra("patientName", userName);
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

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
                PatientsViewHolder viewHolder = new PatientsViewHolder(view);
                return viewHolder;

            }
        };

        myPatientList.setAdapter(adapter);
        adapter.startListening();



        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    search(s.toString());
                } else {
                    search("");
                }

            }
        });

    }


    private void search(String s) {

        Query query = PatientsRef.orderByChild("patientName").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    arrayList.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {

                        final Patient patient = dss.getValue(Patient.class);
                        arrayList.add(patient);
                    }

                    myPatientAdapter myPatientAdapter = new myPatientAdapter(getApplicationContext(),arrayList);
                    myPatientList.setAdapter(myPatientAdapter);
                    myPatientAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    public static class PatientsViewHolder extends RecyclerView.ViewHolder {

        TextView PatientName, patientSeverity, patientRegion, patientDOB;
        ImageView PatientURL;


        public PatientsViewHolder(@NonNull View itemView) {
            super(itemView);

            PatientURL = itemView.findViewById(R.id.profileImage);
            PatientName = itemView.findViewById(R.id.TextViewNameList);
            patientSeverity = itemView.findViewById(R.id.TextViewSeverity);
            patientRegion = itemView.findViewById(R.id.TextViewRegion);
            patientDOB = itemView.findViewById(R.id.TextViewDOB2);


        }
    }

        /*
    private void FirebaseSearch(String s) {
        Query query = PatientsRef.orderByChild("patientName").startAt(s).endAt(s + "\uf8ff");

        FirebaseRecyclerOptions option = new FirebaseRecyclerOptions.Builder<Patient>().setQuery(query, Patient.class).build();

        adapter = new FirebaseRecyclerAdapter<Patient, PatientsViewHolder>(option) {

            @NonNull
            @Override
            public PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
                return new PatientsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PatientsViewHolder holder, int position, @NonNull Patient model) {

                final String patientID = getRef(position).getKey();

                PatientsRef.child(patientID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //getting the values for the recycler view
                        //https://www.youtube.com/watch?v=FFHuYcB3YnU - some code taken from tutorial
                        final String userName = dataSnapshot.child("patientName").getValue().toString();
                        String userRegion = dataSnapshot.child("patientRegion").getValue().toString();
                        String userDOB = dataSnapshot.child("patientDOB").getValue().toString();
                        String userSeverity = dataSnapshot.child("patientSeverity").getValue().toString();
                        String userURL = dataSnapshot.child("imageURL").getValue().toString();

                        uriConvert = Uri.parse(userURL);

                        Glide.with(context)
                                .load(uriConvert)
                                .into(holder.PatientURL);

                        holder.PatientName.setText(userName);
                        holder.patientDOB.setText(userDOB);
                        holder.patientRegion.setText(userRegion);
                        holder.patientSeverity.setText(userSeverity);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Passing the user ID to the next page
                                Intent patientIntent = new Intent(getApplicationContext(), ViewPatientBleeds.class);
                                patientIntent.putExtra("patientName", userName);
                                patientIntent.putExtra("patientID", patientID);
                                startActivity(patientIntent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                    // adapter = new myPatientAdapter(options);
                    // myPatientList.setAdapter(adapter);
                    // adapter.startListening();

                });
            }


        };

        myPatientList.setAdapter(adapter);

    }

         */

}