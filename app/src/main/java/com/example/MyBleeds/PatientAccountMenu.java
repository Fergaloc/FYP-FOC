package com.example.MyBleeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PatientAccountMenu extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";


    EditText editTextName;
    Button  buttonLogOut,buttonAccount,buttonParent,buttonFAQ;

    DatabaseReference databasePatients;

    DatabaseReference databasepatient;

    FirebaseAuth mAuth;
    FirebaseStorage storage;

    StorageReference storageReferencere;
    Context context;
    BottomNavigationView itemSelectedListener;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_account);
        mAuth = FirebaseAuth.getInstance();

        databasepatient = FirebaseDatabase.getInstance().getReference("patients").child("U32N7b9ZetXeQtBx9o9YIZBI7yB2");

        //getting views
        buttonLogOut = (Button) findViewById(R.id.buttonLogoutParent);
        buttonParent = (Button) findViewById(R.id.buttonParentID);
        buttonAccount = (Button) findViewById(R.id.buttonAccountParent);
        itemSelectedListener = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        buttonFAQ = (Button) findViewById(R.id.buttonFAQ) ;



        context = getApplicationContext();
        String uid = FirebaseAuth.getInstance().getUid();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

        } else {
            // No user is signed in
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(PatientAccountMenu.this, welcomepage.class));

        }






        storage = FirebaseStorage.getInstance();
        storageReferencere = storage.getReference();

        //Logs out user and send them to the Log-in page.
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(PatientAccountMenu.this, welcomepage.class));
            }
        });

        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountIntent = new Intent(PatientAccountMenu.this , PatientSettingsActivity.class);
                startActivity(accountIntent);

            }
        });

        buttonParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parentIntent = new Intent(PatientAccountMenu.this, AddParent.class);
                startActivity(parentIntent);

            }
        });


        buttonFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FAQintent = new Intent(PatientAccountMenu.this , patientFAQ.class);
                startActivity(FAQintent);

            }
        });

        //Bottom navigation switch case to decide location based upon selected item
        itemSelectedListener.setSelectedItemId(R.id.ic_account);

        itemSelectedListener.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        String patientnEW = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                        intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intentSettings);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ic_search:
                        String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Intent intent = new Intent(getApplicationContext(), ViewAllBleeds.class);

                        intent.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.ic_account:

                        break;
                }

                return false;
            }
        });












    }


}
