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
import android.widget.AdapterView;
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

public class ParentViewChildHome extends AppCompatActivity {

    //creates string for use in intents
    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    private String patientsID;

    FirebaseAuth mAuth;
    TextView tvPatientName;
    DatabaseReference databaseBleeds;

    Button btnViewAllBleeds,btnReturnViewChildHome;
    Button btnViewHealth;
    String ID,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_child_home);
        mAuth = FirebaseAuth.getInstance();

        btnViewAllBleeds = (Button) findViewById(R.id.btnViewRecent);
        btnViewHealth = (Button) findViewById(R.id.btnViewHealth);
        btnReturnViewChildHome = (Button) findViewById(R.id.btnReturnViewChildHome);


        tvPatientName = (TextView) findViewById(R.id.txtHomeName);
        String PatientName;


        patientsID = getIntent().getExtras().get(PATIENT_ID).toString();
        PatientName = getIntent().getExtras().get(PATIENT_NAME).toString();

        name = PatientName;

        //sets patient name
        tvPatientName.setText(PatientName);

        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(patientsID);


    }


    @Override
    protected void onStart() {
        super.onStart();




        btnViewAllBleeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Passing the user ID to the next page
                Intent patientIntent = new Intent(getApplicationContext(), ParentViewBleeds.class);
                patientIntent.putExtra(PATIENT_NAME, name);
                patientIntent.putExtra(PATIENT_ID, patientsID);
                startActivity(patientIntent);

            }
        });


        btnViewHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Passing the user ID to the next page
                Intent patientIntents = new Intent(getApplicationContext(), ParentViewHealth.class);
                patientIntents.putExtra(PATIENT_NAME, name);
                patientIntents.putExtra(PATIENT_ID, patientsID);
                startActivity(patientIntents);


            }
        });


        btnReturnViewChildHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentViewChildHome.this.onBackPressed();
                finish();
            }
        });



    }


}


