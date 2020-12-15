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
    Button buttonLogOut;

    List<Patient> patients;

    DatabaseReference databasePatients;

    FirebaseAuth mAuth;


    public static final String DOCTOR_ID = "doctorid";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home);
        mAuth = FirebaseAuth.getInstance();

        databasePatients = FirebaseDatabase.getInstance().getReference("doctor");

        buttonViewPatients = (Button) findViewById(R.id.buttonViewPatients);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOutDoctor);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Code to ensure its a doctor that logs in.
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("doctor");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(uid)) {
                    Toast.makeText(DoctorHomeActivity.this, "Logged in", Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(DoctorHomeActivity.this, LogInActivity.class));
                    Toast.makeText(DoctorHomeActivity.this, "You have been Logged Out, please Login via the Patient Portal", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {


            }

        });




        buttonViewPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String doctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(getApplicationContext(), ViewMyPatients.class);

                intent.putExtra(DOCTOR_ID, mAuth.getCurrentUser().getUid());

                startActivity(intent);

            }
        });

        //Logs out user and send them to the Log-in page.
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DoctorHomeActivity.this, LogInActivity.class));
            }
        });


    }

}
