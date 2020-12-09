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

public class ViewPatientBleeds extends AppCompatActivity {

    //creates string for use in intents
    public static final String BLEED_NAME = "bleedname";
    public static final String BLEED_SIDE = "bleedside";
    public static final String BLEED_SEVERITY = "bleedseverity";
    public static final String BLEED_CAUSE = "bleedcause";
    public static final String BLEED_DATE = "bleeddate";

    private String patientsID;

    FirebaseAuth mAuth;

    TextView tvPatientName;

    ListView listViewBleeds;

    DatabaseReference databaseBleeds;
    List<Bleed> bleeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_bleed);
        mAuth = FirebaseAuth.getInstance();

        listViewBleeds = (ListView) findViewById(R.id.listViewPatientBleeds);
        tvPatientName = (TextView) findViewById(R.id.tvPatientName);

        String PatientName;

        patientsID = getIntent().getExtras().get("patientID").toString();

        PatientName = getIntent().getExtras().get("patientName").toString();


        //sets patient name
        tvPatientName.setText(PatientName);

        databaseBleeds = FirebaseDatabase.getInstance().getReference("bleeds").child(patientsID);

        bleeds = new ArrayList<>();

            }


    @Override
    protected void onStart() {
        super.onStart();

        // code to detect changes in values and display all bleed data as per BleedListClass.
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


        //onclick that brings us to bleed page with more details.
        listViewBleeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bleed bleed = (Bleed) parent.getAdapter().getItem(position);

                Intent viewIntent = new Intent(ViewPatientBleeds.this, ViewSingleBleed.class);
                viewIntent.putExtra(BLEED_NAME, bleed.getBleedName());
                viewIntent.putExtra(BLEED_SEVERITY, bleed.getBleedSeverity());
                viewIntent.putExtra(BLEED_SIDE, bleed.getBleedSide());
                viewIntent.putExtra(BLEED_DATE, bleed.getBleedDate());
                viewIntent.putExtra(BLEED_CAUSE, bleed.getBleedCause());

                startActivity(viewIntent);

            }
        });




}


}

