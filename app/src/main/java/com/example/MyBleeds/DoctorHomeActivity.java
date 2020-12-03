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
import java.util.List;

public class DoctorHomeActivity extends AppCompatActivity {

    Button buttonViewPatients;
    Button buttonDoctorSettings;

    List<Patient> patients;

    DatabaseReference databasePatients;

    FirebaseAuth mAuth;


    public static final String DOCTOR_ID = "doctorid";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home);
        mAuth = FirebaseAuth.getInstance();

        databasePatients = FirebaseDatabase.getInstance().getReference("doctor");

        buttonDoctorSettings = (Button) findViewById(R.id.buttonDoctorSettings);
        buttonViewPatients = (Button) findViewById(R.id.buttonViewPatients);


        //Brings us to account settings page
        buttonDoctorSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String doctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intentSettings = new Intent(getApplicationContext(), PatientSettingsActivity.class);

                intentSettings.putExtra(DOCTOR_ID, mAuth.getCurrentUser().getUid());

                startActivity(intentSettings);

            }
        });

        buttonViewPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String doctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), ViewPatients.class);

                intent.putExtra(DOCTOR_ID, mAuth.getCurrentUser().getUid());

                startActivity(intent);

            }
        });


    }
}
