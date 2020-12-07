package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewPatientBleeds extends AppCompatActivity {

    private String patientsID;

    FirebaseAuth mAuth;

    ListView listViewBleeds;

    DatabaseReference databaseBleeds;
    List<Bleed> bleeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_bleed);
        mAuth = FirebaseAuth.getInstance();

        listViewBleeds = (ListView) findViewById(R.id.listViewPatientBleeds);

        patientsID = getIntent().getExtras().get("patientID").toString();

        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(patientsID);

        bleeds = new ArrayList<>();

        Toast.makeText(this,patientsID , Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onStart() {
        super.onStart();

        // code to detect changes in values
        databaseBleeds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bleeds.clear();
                for(DataSnapshot trackSnapshot: dataSnapshot.getChildren()){
                    Bleed bleed = trackSnapshot.getValue(Bleed.class);
                    bleeds.add(bleed);
                }
                BleedList bleedListAdapter = new BleedList(ViewPatientBleeds.this, bleeds);
                listViewBleeds.setAdapter(bleedListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}