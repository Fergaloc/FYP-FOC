package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

//User Home page, navigating mainly
public class Patient_HomeActivity extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    Button buttonAdd;
    Button buttonView;
    Button buttonUserSettings;



    List<Patient> patients;

    DatabaseReference databasePatients;

    FirebaseAuth mAuth;

    BottomNavigationView itemSelectedListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_home);
        mAuth = FirebaseAuth.getInstance();

        databasePatients = FirebaseDatabase.getInstance().getReference("patients");

        buttonAdd = (Button) findViewById(R.id.buttonAddBleed);
        buttonView = (Button) findViewById(R.id.buttonViewBleed);
        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //Brings us to add bleeds page, also works as view bleeds page
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), AddBleedActivity.class);

                intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());


                startActivity(intent);

            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), ViewBleeds.class);

                intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());


                startActivity(intent);

            }
        });


        //Bottom navigation switch case to decide location based upon selected item
        itemSelectedListener.setSelectedItemId(R.id.ic_home);

     itemSelectedListener.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
             switch (menuItem.getItemId()){
                 case R.id.ic_home:
                     break;
                 case R.id.ic_search:
                     String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                     Intent intent = new Intent(getApplicationContext(), ViewBleeds.class);

                     intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                     startActivity(intent);
                     overridePendingTransition(0,0);
                     return true;

                 case R.id.ic_account:

                     String patientnEW = FirebaseAuth.getInstance().getCurrentUser().getUid();
                     Intent intentSettings = new Intent(getApplicationContext(), PatientSettingsActivity.class);

                     intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                     startActivity(intentSettings);
                     overridePendingTransition(0,0);
                     return true;
             }

             return false;
         }
     });



    }

}