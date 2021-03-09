package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPatientHome extends AppCompatActivity {

    //creates string for use in intents
    public static final String PATIENT_ID = "patientID";
    public static final String BLEED_ID = "bleedid";
    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";


    private Uri uriConvert;
    private String imgCheck;


    private String patientsID;

    FirebaseAuth mAuth;
    TextView tvPatientName;
    DatabaseReference databaseBleeds;

    Button btnViewAllBleeds,btnDocBack;
    Button btnViewHealth;
    String ID,name;

    TextView txtName,txtDOB, txtSeverity,txtRegion;

    CircleImageView imgPatientHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_home);
        mAuth = FirebaseAuth.getInstance();

        btnViewAllBleeds = (Button) findViewById(R.id.btnViewRecent);
        btnViewHealth = (Button) findViewById(R.id.btnViewHealth);

        txtName = (TextView) findViewById(R.id.txtHomeName);
        txtSeverity = (TextView) findViewById(R.id.txtHomeSeverity);
        txtDOB = (TextView) findViewById(R.id.txtDOBhome);
        txtRegion = (TextView) findViewById(R.id.txtRegionHome);
        imgPatientHome = (CircleImageView) findViewById(R.id.imgPatientHome);
        btnDocBack = (Button) findViewById(R.id.btnDocBack);

        String PatientName;

        patientsID = getIntent().getExtras().get("patientID").toString();
        // PatientName = getIntent().getExtras().get("patientName").toString();

        ID = patientsID;

        //  name = PatientName;

        //sets patient name
        //  tvPatientName.setText(PatientName);

        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(patientsID);



        DatabaseReference dbPatientDetails;
        dbPatientDetails = FirebaseDatabase.getInstance().getReference().child("patients").child(patientsID);

        dbPatientDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Patient patient = dataSnapshot.getValue(Patient.class);

                txtName.setText(patient.getPatientName());
                txtDOB.setText(patient.getPatientDOB());
                txtRegion.setText(patient.getPatientRegion());
                txtSeverity.setText(patient.getPatientSeverity());


                imgCheck = patient.getImageURL();
                if(imgCheck!= null) {
                    uriConvert = Uri.parse(imgCheck);
                }
                if(imgCheck == null){
                    imgPatientHome.setVisibility(View.GONE);
                }

                Glide.with(getApplicationContext()).load(uriConvert).into(imgPatientHome);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnDocBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getApplicationContext(), DoctorHomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();




        btnViewAllBleeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Passing the user ID to the next page
                Intent patientIntent = new Intent(getApplicationContext(), ViewPatientBleeds.class);
                //  patientIntent.putExtra("patientName", name);
                patientIntent.putExtra(PATIENT_ID, ID);
                startActivity(patientIntent);

            }
        });


        btnViewHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Passing the user ID to the next page
                Intent patientIntents = new Intent(getApplicationContext(), ViewPatientHealth.class);
                // patientIntents.putExtra("patientName", name);
                patientIntents.putExtra("patientID", ID);
                startActivity(patientIntents);


            }
        });




    }


}