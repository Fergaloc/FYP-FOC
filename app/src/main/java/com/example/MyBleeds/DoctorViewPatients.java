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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


//https://www.youtube.com/watch?v=FFHuYcB3YnU
//Code to display patients from Firebase and be able to view the patients bleeds via on click listener

public class DoctorViewPatients extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private String currentUserID;

    DatabaseReference SearchRef,allPs;
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> filterkeys = new ArrayList<>();

    List<Patient> patients;
    List<Patient> patientsList;

    List<Patient> filterPatient;
    Context context;

    EditText SearchText;
    SearchView searchView;

    Query allPatients;
    ListView lstPatients;

    int textlength = 0;

    Button btnBack;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_viewpatients);

        final List<String> keys = new ArrayList<>();

        lstPatients = (ListView) findViewById(R.id.lstPatients);
        SearchText = (EditText) findViewById(R.id.edtName);
        btnBack = (Button) findViewById(R.id.btnBack);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        patients = new ArrayList<>();
        patientsList = new ArrayList<>();
        filterPatient = new ArrayList<>();

        context = getApplicationContext();

        allPs = FirebaseDatabase.getInstance().getReference().child("patients");

        //load in the patients for the logged in doctor
        final Query all = allPs.orderByChild("doctorID").equalTo(currentUserID);
        all.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientsList.clear();
                for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()){
                    Patient patiented = patientSnapshot.getValue(Patient.class);
                    patients.add(patiented);
                    keys.add(patientSnapshot.getKey());
                    final PatientList patientListAdapter = new PatientList(DoctorViewPatients.this, patients);
                    lstPatients.setAdapter(patientListAdapter);
                    patientListAdapter.notifyDataSetChanged();

                    lstPatients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Patient patient = (Patient) parent.getAdapter().getItem(position);
                            final  String patientID =  all.getRef().getKey();
                            String myKey = keys.get(position);

                            Intent patientIntent = new Intent(getApplicationContext(), ViewPatientHome.class);
                            patientIntent.putExtra("patientID", myKey);
                            startActivity(patientIntent);
                            finish();

                        }
                    });









                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                textlength = SearchText.getText().length();

                String name = SearchText.getText().toString();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                all.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        patientsList.clear();
                        patients.clear();
                        for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()){
                            Patient patiented = patientSnapshot.getValue(Patient.class);

                            if(patiented.getPatientName().contains(s)) {
                                patients.add(patiented);
                                final PatientList patientListAdapter = new PatientList(DoctorViewPatients.this, patients);
                                lstPatients.setAdapter(patientListAdapter);
                                patientListAdapter.notifyDataSetChanged();
                                filterkeys.add(patientSnapshot.getKey());


                                lstPatients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Patient patient = (Patient) parent.getAdapter().getItem(position);
                                        final  String patientID =  all.getRef().getKey();
                                        String myKeys = filterkeys.get(position);

                                        Intent patientIntent = new Intent(getApplicationContext(), ViewPatientHome.class);
                                        patientIntent.putExtra("patientID", myKeys);
                                        startActivity(patientIntent);
                                        finish();


                                    }
                                });





                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

                patientsList.clear();

            }
        });



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorViewPatients.this.onBackPressed();
                finish();
            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();



    }
}