package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientSettingsActivity extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextName;
    Button buttonUpdate, buttonHome, buttonLogOut;
    Spinner spinnerRegion;

    DatabaseReference databasePatients;

    ListView listViewArtists;

    FirebaseAuth mAuth;

    List<Patient> patients;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_settings);
        mAuth = FirebaseAuth.getInstance();

        databasePatients = FirebaseDatabase.getInstance().getReference("patients");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextFirstName);
        spinnerRegion = (Spinner) findViewById(R.id.spinnerRegion);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdatePatient);
        buttonHome = (Button) findViewById(R.id.buttonHome);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);



        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        patients = new ArrayList<>();
            //updating a patient settings + ADD INTENT to bring to main page
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextName.getText().toString().trim();
                    String region = spinnerRegion.getSelectedItem().toString();

                    if(TextUtils.isEmpty(name)){
                        editTextName.setError("Name Required");
                        return;
                    }

                    updatePatient(name, region);

                }


            });

        //Bringing user to home page, doesnt save data
        buttonHome.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

            intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

            startActivity(intentSettings);

        }

    });


        //Logs out user and send them to the Log-in page.
            buttonLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(PatientSettingsActivity.this, LogInActivity.class));
                }
            });




        }

    private boolean updatePatient(String name, String region){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients").child(uid);

        Patient patient = new Patient( name , region);

        //overide with new patient

        databaseReference.setValue(patient);

        return true;

    }





}
