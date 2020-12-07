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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class ViewPatients extends AppCompatActivity {


    EditText editText;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;

    DatabaseReference databasePatients;
    StorageReference storage;

    DatabaseReference rootRef;

    DatabaseReference patientRef;

    private ArrayList<Patient> patientArrayList;
    private RecyclerPatientAdapter recyclerPatientAdapter;
    private Context mContext;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);
        mAuth = FirebaseAuth.getInstance();


        //Code to search by Name of patient
        //https://codinginflow.com/tutorials/android/recyclerview-edittext-search
        EditText editText = (EditText) findViewById(R.id.SearchText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Intent intent = getIntent();

        String id = intent.getStringExtra(DoctorHomeActivity.DOCTOR_ID);
        databasePatients = FirebaseDatabase.getInstance().getReference("patients").child(id);

        patientArrayList = new ArrayList<>();

        GetDataFromFirebase();

        ClearAll();

    }

    private void filter(String text){

        ArrayList<Patient> filteredList = new ArrayList<>();

        for(Patient item: patientArrayList) {
            if (item.getPatientName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        recyclerPatientAdapter.filterlist(filteredList);

    }


    //https://www.youtube.com/watch?v=BrDX6VTgTkg
    //code to use a recycler view to get data from firevase
    private void GetDataFromFirebase() {

        rootRef = FirebaseDatabase.getInstance().getReference("patients");
        patientRef = rootRef.child("U32N7b9ZetXeQtBx9o9YIZBI7yB2");

        Query query =  patientRef;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Patient patient = new Patient();

                    patient.setImageURL(snapshot.child("imageURL").getValue().toString());
                    patient.setPatientName(snapshot.child("patientName").getValue().toString());
                    patient.setPatientRegion(snapshot.child("patientRegion").getValue().toString());
                    patient.setPatientDOB(snapshot.child("patientDOB").getValue().toString());
                    patient.setPatientSeverity(snapshot.child("patientSeverity").getValue().toString());

                    patientArrayList.add(patient);

                }

                recyclerPatientAdapter = new RecyclerPatientAdapter(getApplicationContext(),patientArrayList);
                recyclerView.setAdapter(recyclerPatientAdapter);
                recyclerPatientAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Make sure array list is empty
    private void ClearAll(){

        if(patientArrayList != null){
            patientArrayList.clear();

            if (recyclerPatientAdapter != null){
                recyclerPatientAdapter.notifyDataSetChanged();
            }
        }
        patientArrayList = new ArrayList<>();

    }

}
