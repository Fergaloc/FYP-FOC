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

public class View_Patients extends AppCompatActivity {

    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    TextView textView;
    EditText SearchPatientsText;

    DatabaseReference databasePatients;
    StorageReference storage;

    private ArrayList<Patient> patientArrayList;
    private RecyclerPatientAdapter recyclerPatientAdapter;
    private Context mContext;







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textView= (TextView) findViewById(R.id.textViewPatients);
        SearchPatientsText = (EditText) findViewById(R.id.SearchPatientsText);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        databasePatients = FirebaseDatabase.getInstance().getReference();

        patientArrayList = new ArrayList<>();

        GetDataFromFirebase();

        ClearAll();




    }

    private void GetDataFromFirebase() {

        Query query =  databasePatients.child("patients").child(mAuth.getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Patient patient = new Patient();

                    patient.setImageURL(snapshot.child("imageURL").getValue().toString());
                    patient.setPatientName(snapshot.child("patientName").getValue().toString());

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
