package com.example.MyBleeds;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class ViewBleeds extends AppCompatActivity {

    public static final String PATIENT_NAME = "patientname";
    public static final String PATIENT_ID = "patientid";

    FirebaseAuth mAuth;

    ListView listViewBleeds;
    Button buttonHome;

    DatabaseReference databaseBleeds;
    List<Bleed> bleeds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bleeds_activity);
        mAuth = FirebaseAuth.getInstance();

        listViewBleeds = (ListView) findViewById(R.id.listViewBleeds);
        buttonHome = (Button) findViewById(R.id.buttonHome);

        Intent intent = getIntent();

        bleeds = new ArrayList<>();

        String id = intent.getStringExtra(PatientSettingsActivity.PATIENT_ID);
        String name = intent.getStringExtra(PatientSettingsActivity.PATIENT_NAME);

        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(id);


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patient = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intentSettings = new Intent(getApplicationContext(), Patient_HomeActivity.class);

                intentSettings.putExtra(PATIENT_ID, mAuth.getCurrentUser().getUid());

                startActivity(intentSettings);

            }

        });

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
                BleedList bleedListAdapter = new BleedList(ViewBleeds.this, bleeds);
                listViewBleeds.setAdapter(bleedListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
